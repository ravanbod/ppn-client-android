package com.behradrvb.ppnclient.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.behradrvb.ppnclient.interfaces.PPNConnectionInterface;
import com.behradrvb.ppnclient.interfaces.PPNMessagesInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class PPNConnection {
    private String host;
    private int port;
    private Socket socket = null;
    private Thread connectionThread = null;
    private PrintWriter output = null;
    private InputStream input = null;
    private Thread inputThread = null;
    private StringBuilder inputMessege = null;
    private int BUFFER_SIZE = 1024;
    private PPNConnectionInterface ppnConnectionInterface;
    private PPNMessagesInterface ppnMessagesInterface;

    /**
     * This method initializes data for prepare new connection.
     */
    public PPNConnection(String host, int port, @Nullable PPNConnectionInterface ppnConnectionInterface, @Nullable PPNMessagesInterface ppnMessagesInterface) {
        this.host = host;
        this.port = port;
        this.ppnConnectionInterface = ppnConnectionInterface;
        this.ppnMessagesInterface = ppnMessagesInterface;
    }

    /**
     * this function creates thread for connection to server.
     */
    public void connect(String host, int port) {
        connectionThread = new Thread(
                new Connection(
                        host,
                        port)
        );
        connectionThread.start();
    }

    /**
     * This function close connection if that is open.
     */
    public void disconnect() throws IOException {
        if (socket.isConnected()) {
            socket.close();
            Log.e("PPN Connection", "Connection closed.");
            ppnConnectionInterface.OnConnectionClosed();
        }
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
                ppnConnectionInterface.OnNewConnectionEstablished();
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
                    ppnMessagesInterface.OnNewMessageReceived(inputMessege.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
