package kr.co.aperturedev.callmyadminc.internet.realtime.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

import kr.co.aperturedev.callmyadminc.core.context.excuter.ServerRequestExcuter;
import kr.co.aperturedev.callmyadminc.internet.realtime.listeners.OnResponseListener;
import kr.co.aperturedev.callmyadminc.internet.realtime.packet.ResponsePacket;

/**
 * Created by 5252b on 2017-11-05.
 */

public class OnResponseBroadcast extends BroadcastReceiver {
    private static HashMap<String, OnResponseListener> listeners = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(! intent.getAction().equals("kr.co.aperturedev.callmyadminc.onreceive")) return;

        String jsonScript = intent.getStringExtra("json-script");

        try {
            ResponsePacket respPacket = new ResponsePacket(jsonScript);
            OnResponseListener listener = listeners.get(respPacket.getRequestCode());

            if(listener != null) {
                listener.onResponse(respPacket);
            } else {
                String header = respPacket.getHeader();
                if (header.equals("server-request")) {
                    ServerRequestExcuter serverRequestExcuter = new ServerRequestExcuter(respPacket, context);
                    serverRequestExcuter.requestExqute();
                }
            }
        } catch(Exception ex) {
            Log.e("cma", "Packet 누락!!" + jsonScript.length() + "Bytes");
            ex.printStackTrace();
            return;
        }
    }

    public static void registerListener(String reqId, OnResponseListener listener) {
        OnResponseBroadcast.listeners.put(reqId, listener);
    }

    public static void unregisterListener(String reqId) {
        try {
            OnResponseBroadcast.listeners.remove(reqId);
        } catch(Exception ex) {}
    }
}
