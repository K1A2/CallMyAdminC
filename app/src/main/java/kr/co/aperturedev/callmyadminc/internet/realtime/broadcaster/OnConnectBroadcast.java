package kr.co.aperturedev.callmyadminc.internet.realtime.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.UUID;

import kr.co.aperturedev.callmyadminc.internet.realtime.RealtimeService;
import kr.co.aperturedev.callmyadminc.internet.realtime.engine.NetRequester;
import kr.co.aperturedev.callmyadminc.internet.realtime.engine.NetResponser;
import kr.co.aperturedev.callmyadminc.internet.realtime.listeners.OnConnectListener;
import kr.co.aperturedev.callmyadminc.internet.realtime.listeners.OnResponseListener;
import kr.co.aperturedev.callmyadminc.internet.realtime.packet.RequestPacket;
import kr.co.aperturedev.callmyadminc.internet.realtime.packet.ResponsePacket;
import kr.co.aperturedev.callmyadminc.module.authme.AuthmeModule;
import kr.co.aperturedev.callmyadminc.module.authme.AuthmeObject;
import kr.co.aperturedev.callmyadminc.module.authme.OnAuthmeListener;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigKeys;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigManager;

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
            return;
        }

        // 연결 성공시 인증 절차를 밟는다.
        ConfigManager cfgMgr = new ConfigManager(ConfigKeys.KEY_REPOSITORY, context);
        String uuid = cfgMgr.get().getString(ConfigKeys.KEY_DEVICE_UUID, null);

        if(uuid == null) {
            // 인증되지 않은 장치. 인증 취소
            Intent noAuthBdc = new Intent("kr.co.aperturedev.callmyadminc.onauthme");
            noAuthBdc.putExtra("is-auth", false);
            noAuthBdc.putExtra("reason", "Unauthorized device");

            context.sendBroadcast(noAuthBdc);
            return;
        }

        // 인증 가능 장치는 인증서 가져옴.
        AuthmeModule authme = new AuthmeModule(uuid, new RealtimeAuthWorker(context));
        authme.run();
    }

    class RealtimeAuthWorker implements OnAuthmeListener {
        private Context context = null;
        private AuthmeObject authmeObj = null;

        public RealtimeAuthWorker(Context context) {
            this.context = context;
        }

        @Override
        public void onAuthme(boolean isSucc, AuthmeObject authme) {
            if(!isSucc) {
                // 인증서 호출 실패시 중지
                return;
            }

            this.authmeObj = authme;

            // 리얼타임 서버 인증
            String reqId = UUID.randomUUID().toString().replaceAll("-", "");
            try {
                RequestPacket reqPacket = new RequestPacket("mb-connection-auth");
                reqPacket.setHeader("client-request");
                reqPacket.setArgs(new Object[] {
                        this.authmeObj.getUuid()
                });
                reqPacket.setRequestCode(reqId);

                // 리스너 등록
                OnResponseBroadcast.registerListener(reqId, new OnResponseHandler());
                NetRequester requester = new NetRequester(NetResponser.responser.getSocket(), null, reqPacket.getJSONScript());
                requester.start();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        class OnResponseHandler implements OnResponseListener {
            @Override
            public void onResponse(ResponsePacket respPacket) {
                try {
                    Log.d("cma", "인증기에서 인증을 받았어요!!" + respPacket.getMessage());
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
