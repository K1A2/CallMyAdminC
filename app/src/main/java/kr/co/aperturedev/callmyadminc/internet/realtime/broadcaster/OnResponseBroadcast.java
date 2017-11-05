package kr.co.aperturedev.callmyadminc.internet.realtime.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

/**
 * Created by 5252b on 2017-11-05.
 */

public class OnResponseBroadcast extends BroadcastReceiver {
    private static HashMap<String, OnResponseBroadcast> listeners = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getAction();
        String respJSONScript = null;

        if (name.equals("kr.co.aperturedev.callmyadminc.onresponse")) {
            respJSONScript = intent.getStringExtra("");
        }
    }
}
