package kr.co.aperturedev.callmyadminc.internet.realtime;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import kr.co.aperturedev.callmyadminc.internet.realtime.engine.NetResponser;

/**
 * Created by 5252b on 2017-11-05.
 */

public class RealtimeService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서버와 연결을 처리 합니다.
        Log.d("cma", "리얼타임 서비스 실행!");

        if(NetResponser.responser != null) {
            Log.d("cma", "이미 연결된 상태, Skip");
            return START_STICKY;
        }

        SocketAddress addr = new InetSocketAddress(NetworkHost.SERVER_HOST, NetworkHost.SERVER_PORT);
        RealtimeConnector connector = new RealtimeConnector(addr);
        connector.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class RealtimeConnector extends Thread {
        private SocketAddress addr = null;

        public RealtimeConnector(SocketAddress addr) {
            this.addr = addr;
        }

        @Override
        public void run() {
            boolean isConnect = false;

            try {
                // 서버와 연결 시도
                Socket socket = new Socket();
                socket.connect(this.addr, NetworkHost.TIME_OUT);
                Thread.sleep(2000);

                new NetResponser(socket, getApplicationContext()).start();

                isConnect = true;
            } catch (Exception ex) {
                // 서버와 연결 할 수 없슴.
                isConnect = false;
            }

            Intent intent = new Intent("kr.co.aperturedev.callmyadminc.onconnect");
            intent.putExtra("is-connected", isConnect);
            sendBroadcast(intent);
        }
    }






    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
