package com.behradrvb.ppnclient;

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
    private Socket socket;
    private PrintWriter output;
    private InputStream input;
    private StringBuilder inputMessege;
    private int BUFFER_SIZE = 1024;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            new Thread(
                    new Connection(
                            intent.getExtras().getString("host"),
                            intent.getExtras().getInt("port"))
            ).start(); //Start new connection
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
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
                new Thread(new Input()).start();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("fuck", e.getMessage());
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

}
