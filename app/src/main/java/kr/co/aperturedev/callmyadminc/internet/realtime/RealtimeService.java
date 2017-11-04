package kr.co.aperturedev.callmyadminc.internet.realtime;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by 5252b on 2017-11-04.
 */

public class RealtimeService extends Service {
    private RealtimeThread connectThread = null;
    private boolean isConnected = false;


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e("리얼타임", "리얼타임 시작");
        if(!isConnected) {
            this.connectThread = new RealtimeThread();
            this.connectThread.start();
        }

        return START_REDELIVER_INTENT;
    }

    class RealtimeThread extends Thread {
        private SocketAddress sockAddr = null;
        private Socket socket = null;

        public RealtimeThread() {
            this.sockAddr = new InetSocketAddress(ServerHost.SERVER_HOST, ServerHost.SERVER_PORT);
            this.socket = new Socket();
        }

        @Override
        public void run() {
            Log.e("스레드", "리얼타임 스레드 시작");
            try {
                this.socket.connect(this.sockAddr, ServerHost.TIME_OUT);

                new NetRequester(socket);
                new NetResponser(socket).start();

                isConnected = true;
            } catch(Exception ex) {
                isConnected = false;
                stopSelf();
            }

            Intent intent = new Intent("kr.co.aperturedev.callmyadminc.onconnect");
            intent.putExtra("is-connect", isConnected);
            sendBroadcast(intent);
        }
    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
