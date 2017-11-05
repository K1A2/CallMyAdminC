package kr.co.aperturedev.callmyadminc.internet.realtime.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import kr.co.aperturedev.callmyadminc.internet.realtime.RealtimeService;
import kr.co.aperturedev.callmyadminc.internet.realtime.listeners.OnConnectListener;

/**
 * Created by 5252b on 2017-11-05.
 */

public class OnConnectBroadcast extends BroadcastReceiver {
    public static OnConnectListener listener = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getAction();
        if(!name.equals("kr.co.aperturedev.callmyadminc.onconnect"))  return;

        boolean isConnect = intent.getBooleanExtra("is-connected", false);
        Log.d("cma", "OnConnect 호출 됨. : " + isConnect);

        try {
            if (OnConnectBroadcast.listener != null) {
                OnConnectBroadcast.listener.onConnect(isConnect);
                OnConnectBroadcast.listener = null;
            }
        } catch(Exception ex) {}

        if(!isConnect) {
            // 연결 실패? 재연결 시도
            Intent connector = new Intent(context, RealtimeService.class);
            context.startService(connector);
        }
    }
}
