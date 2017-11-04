package kr.co.aperturedev.callmyadminc.internet.realtime.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TestReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getAction();
        if (name.equals("kr.co.aperturedev.callmyadminc.onconnect")) {
            Toast.makeText(context, String.valueOf(intent.getBooleanExtra("is-connect", false)), Toast.LENGTH_SHORT).show();
        }
    }
}
