package kr.co.aperturedev.callmyadminc.internet.realtime;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
            try {
                SocketAddress sockAddr = new InetSocketAddress(ServerHost.SERVER_HOST, ServerHost.SERVER_PORT);
                this.socket.connect(sockAddr, ServerHost.TIME_OUT);

                NetRequester requester = new NetRequester(socket);
                NetResponser responser = new NetResponser(socket);
                responser.start();

                isConnected = true;
            } catch(Exception ex) {
                stopSelf();
            }
        }
    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
