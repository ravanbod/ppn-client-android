package com.behradrvb.ppnclient;

import android.content.Context;
import android.content.SharedPreferences;

import com.behradrvb.ppnclient.interfaces.PPNConnectionInterface;
import com.behradrvb.ppnclient.interfaces.PPNMessagesInterface;
import com.behradrvb.ppnclient.services.PPNConnection;

import java.io.IOException;

final public class PPN {
    private static String host;
    private static int port;
    private static boolean enable;
    private static PPNConnection ppnConnection = null;

    /**
     * This function saves and initializes data ...
     */
    public static void init(Context context, String host, int port, boolean enable) {
        PPN.host = host;
        PPN.port = port;
        PPN.enable = enable;
        savePreferences(context);
    }

    /**
     * This function starts connection between client and server.
     */
    public static void execute(Context context, PPNConnectionInterface ppnConnectionInterface, PPNMessagesInterface ppnMessagesInterface) {
        if(ppnConnectionInterface == null)
            ppnConnectionInterface = new PPNConnectionInterface() {
                @Override
                public void onNewConnectionEsatblished() {

                }

                @Override
                public void OnConnectionClosed() {

                }
            };
        if(ppnMessagesInterface == null)
            ppnMessagesInterface = new PPNMessagesInterface() {
                @Override
                public void OnNewMessageReceived(String msg) {

                }
            };

        ppnConnection = new PPNConnection(host, port, ppnConnectionInterface, ppnMessagesInterface);
        readPreferences(context);
        if (enable)
            ppnConnection.connect(host, port);
        else if (ppnConnection.started) {
            try {
                ppnConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function closes connection between client and server.
     */
    public static void disconnect() {
        if (ppnConnection != null) {
            try {
                ppnConnection.disconnect();
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
        editor.putBoolean("enable", enable);
        editor.apply();
    }

    /**
     * This function reads saved preferences and init variables of this class.
     */
    private static void readPreferences(Context context) {
        SharedPreferences s = context.getSharedPreferences("PPN", Context.MODE_PRIVATE);
        host = s.getString("host", "");
        port = s.getInt("port", 25088);
        enable = s.getBoolean("enable", true);
    }
}
