package kr.co.aperturedev.callmyadminc.internet.realtime.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kr.co.aperturedev.callmyadminc.internet.realtime.RealtimeService;

/**
 * Created by 5252b on 2017-11-06.
 */

public class OnBootingBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // 안드로이드 부팅 시 접속 서비스 실행
            Intent req = new Intent(context, RealtimeService.class);
            context.startService(req);
        }
    }
}
