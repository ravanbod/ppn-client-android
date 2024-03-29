package com.behradrvb.ppnclient.services;

import android.util.Log;

import com.behradrvb.ppnclient.interfaces.PPNConnectionInterface;
import com.behradrvb.ppnclient.interfaces.PPNMessagesInterface;
import com.behradrvb.ppnclient.models.Message;
import com.behradrvb.ppnclient.models.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PPNConnection {
    private Server server;
    private Socket socket = null;
    private OutputStream output = null;
    private InputStream input = null;
    private PPNConnectionInterface ppnConnectionInterface;
    private PPNMessagesInterface ppnMessagesInterface;

    /**
     * This method initializes data for prepare new connection.
     */
    public PPNConnection(Server server, PPNConnectionInterface ppnConnectionInterface, PPNMessagesInterface ppnMessagesInterface) {
        this.server = server;
        this.ppnConnectionInterface = ppnConnectionInterface;
        this.ppnMessagesInterface = ppnMessagesInterface;
    }

    /**
     * this function creates thread for connection to server.
     */
    public void connect() {
        ppnConnectionInterface.OnTry();
        new Thread(new Connection(server.getHost(), server.getPort(), server.getSession_id())).start();
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
        private String session_id;

        Connection(String host, int port, String session_id) {
            this.host = host;
            this.port = port;
            this.session_id = session_id;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(this.host, this.port);
                output = socket.getOutputStream();
                input = socket.getInputStream();
                output.write(session_id.getBytes());
                Log.e("PPN Connection", "Connected to " + this.host + ":" + this.port);
                ppnConnectionInterface.OnNewConnectionEstablished();
                new Thread(new Input()).start();
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
                    StringBuilder inputMessage = new StringBuilder();
                    int BUFFER_SIZE = 1024;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int read;
                    while ((read = input.read(buffer)) != -1) {
                        inputMessage.append(new String(buffer, 0, read));
                        try {
                            new JSONObject(inputMessage.toString()); // this cmd Checks if message is valid, breaks the loop and continues code to do something
                            break;
                        } catch (JSONException ignored) {
                        }
                    }
                    if (inputMessage.toString().isEmpty()){
                        disconnect();
                        break;
                    }
                    else
                        ppnMessagesInterface.OnNewMessageReceived(
                                new Message(
                                        inputMessage.toString()
                                )
                        );
                    if (!socket.isConnected()) //Just was added for removing warning
                        break;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
