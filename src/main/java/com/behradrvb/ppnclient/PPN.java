package com.behradrvb.ppnclient;

import android.content.Context;
import android.content.SharedPreferences;

import com.behradrvb.ppnclient.interfaces.PPNConnectionInterface;
import com.behradrvb.ppnclient.interfaces.PPNMessagesInterface;
import com.behradrvb.ppnclient.models.Message;
import com.behradrvb.ppnclient.models.Server;
import com.behradrvb.ppnclient.services.PPNConnection;

import java.io.IOException;

final public class PPN {
    private static Server server = null;
    private static PPNConnection ppnConnection = null;

    /**
     * This function saves and initializes data ...
     */
    public static void init(Context context, String host, int port) {
        server = new Server(host, port);
        savePreferences(context);
    }

    /**
     * This function starts connection between client and server.
     */
    public static void execute(Context context, PPNConnectionInterface ppnConnectionInterface, PPNMessagesInterface ppnMessagesInterface) {
        if (ppnConnectionInterface == null) //Set interface empty if user didn't init that.
            ppnConnectionInterface = new PPNConnectionInterface() {
                @Override
                public void OnTry() {

                }

                @Override
                public void OnNewConnectionEstablished() {

                }

                @Override
                public void OnConnectionClosed() {

                }
            };
        if (ppnMessagesInterface == null) //Set interface empty if user didn't init that.
            ppnMessagesInterface = new PPNMessagesInterface() {
                @Override
                public void OnNewMessageReceived(Message msg) {

                }
            };

        readPreferences(context);
        if (ppnConnection == null) {
            ppnConnection = new PPNConnection(server, ppnConnectionInterface, ppnMessagesInterface);
            ppnConnection.connect();
        }

    }

    /**
     * This function closes connection between client and server.
     */
    public static void disconnect() {
        if (ppnConnection != null) {
            try {
                ppnConnection.disconnect();
                ppnConnection = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function saves data in SharedPreferences
     */
    private static void savePreferences(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("PPN", Context.MODE_PRIVATE).edit();
        editor.putString("host", server.getHost());
        editor.putInt("port", server.getPort());
        editor.apply();
    }

    /**
     * This function reads saved preferences and init variables of this class.
     */
    private static void readPreferences(Context context) {
        SharedPreferences s = context.getSharedPreferences("PPN", Context.MODE_PRIVATE);
        server = new Server(s.getString("host", ""), s.getInt("port", 25088));
    }
}
