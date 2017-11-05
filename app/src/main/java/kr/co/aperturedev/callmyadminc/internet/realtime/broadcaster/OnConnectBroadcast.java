package kr.co.aperturedev.callmyadminc.internet.realtime.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import kr.co.aperturedev.callmyadminc.internet.realtime.OnConnectListener;

public class OnConnectBroadcast extends BroadcastReceiver {
    private static ArrayList<OnConnectListener> listeners = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getAction();
        if (name.equals("kr.co.aperturedev.callmyadminc.onconnect")) {
            if(listeners == null) return;

            // 모든 등록된 리스너에게 브로드캐스팅 합니다.
            boolean isConnect = intent.getBooleanExtra("is-connect", false);
            for(OnConnectListener lis : listeners) {
                lis.onConnect(isConnect);
            }

            Log.d("cma", "총 : " + listeners.size() + " 개의 리스너에게 전달");
        }
    }

    public static void addListener(OnConnectListener listener) {
        // 전역 변수 이므로 앱이 꺼질 경우 초기화 됩니다.
        listeners.add(listener);
    }

    public static void clearListener() {
        if(listeners != null) {
            listeners.clear();
        }
    }
}
