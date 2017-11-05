package kr.co.aperturedev.callmyadminc.internet.realtime.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import kr.co.aperturedev.callmyadminc.internet.realtime.RealtimeService;

/**
 * Created by 5252b on 2017-11-05.
 */

public class OnDisconnectBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getAction();
        if(!name.equals("kr.co.aperturedev.callmyadminc.ondisconnect"))  return;

        Log.d("cma", "OnDisconnect 호출 됨!!!");

        // 네트워크 끊어짐. 다시 연결 함
        Intent req = new Intent(context, RealtimeService.class);
        context.sendBroadcast(req);
    }
}
