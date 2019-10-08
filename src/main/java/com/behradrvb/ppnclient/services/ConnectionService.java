package com.behradrvb.ppnclient.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionService extends Service {
    public static boolean started = false;
    public static Socket socket = null;
    private Thread connectionThread;
    private PrintWriter output = null;
    private InputStream input = null;
    private Thread inputThread;
    private StringBuilder inputMessege;
    private int BUFFER_SIZE = 1024;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            connectionThread = new Thread(
                    new Connection(
                            intent.getExtras().getString("host"),
                            intent.getExtras().getInt("port"))
            );
            connectionThread.start(); //Start new connection
        } catch (Exception e) {
            e.printStackTrace();
        }
        started = true;
        Log.e("PPN Connection", "ConnectionService started.");
        return START_STICKY;
    }

    /**
     * Start this runnable class in thread to start new connection between application and server
     */
    private class Connection implements Runnable {
        private String host;
        private int port;

        Connection(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(this.host, this.port);
                output = new PrintWriter(socket.getOutputStream());
                input = socket.getInputStream();
                Log.e("PPN Connection", "Connected to " + this.host + ":" + this.port);
                inputThread = new Thread(new Input());
                inputThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Start this runnable class in thread to start getting data from server
     */
    private class Input implements Runnable {

        @Override
        public void run() {
            Log.e("PPN Connection", "Starting to listening to server...");
            while (true) {
                try {
                    inputMessege = new StringBuilder();
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int read;
                    while ((read = input.read(buffer)) != -1) {
                        inputMessege.append(new String(buffer, 0, read));
                        try {
                            new JSONObject(inputMessege.toString()); // this cmd Checks if message is valid, breaks the loop and continues code to do something
                            break;
                        } catch (JSONException je) {
                            continue;
                        }
                    }
                    Log.e("msg", inputMessege.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("PPN Connection", "ConnectionService stopped!");
        started = false;
        try {
            if (socket != null && socket.isConnected())
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendBroadcast(new Intent(this, RestarterBroadcast.class));
    }
}
