package com.behradrvb.ppnclient;

import android.content.Context;
import android.content.SharedPreferences;

import com.behradrvb.ppnclient.interfaces.PPNConnectionInterface;
import com.behradrvb.ppnclient.interfaces.PPNMessagesInterface;
import com.behradrvb.ppnclient.services.PPNConnection;

import java.io.IOException;

final public class PPN {
    private static String host = null;
    private static int port;
    private static PPNConnection ppnConnection = null;

    /**
     * This function saves and initializes data ...
     */
    public static void init(Context context, String host, int port) {
        PPN.host = host;
        PPN.port = port;
        savePreferences(context);
    }

    /**
     * This function starts connection between client and server.
     */
    public static void execute(Context context, PPNConnectionInterface ppnConnectionInterface, PPNMessagesInterface ppnMessagesInterface) {
        if (ppnConnectionInterface == null)
            ppnConnectionInterface = new PPNConnectionInterface() {
                @Override
                public void OnNewConnectionEstablished() {

                }

                @Override
                public void OnConnectionClosed() {

                }
            };
        if (ppnMessagesInterface == null)
            ppnMessagesInterface = new PPNMessagesInterface() {
                @Override
                public void OnNewMessageReceived(String msg) {

                }
            };

        if (host != null)
            readPreferences(context);
        if (ppnConnection == null) {
            ppnConnection = new PPNConnection(host, port, ppnConnectionInterface, ppnMessagesInterface);
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
        editor.putString("host", host);
        editor.putInt("port", port);
        editor.apply();
    }

    /**
     * This function reads saved preferences and init variables of this class.
     */
    private static void readPreferences(Context context) {
        SharedPreferences s = context.getSharedPreferences("PPN", Context.MODE_PRIVATE);
        host = s.getString("host", "");
        port = s.getInt("port", 25088);
    }
}
