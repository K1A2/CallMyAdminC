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

        Log.d("cma", "서비스 실행!!!");

        if(NetResponser.responser == null || !this.connectThread.isAlive()) {
            Log.d("cma", "연결 시도");
            this.connectThread = new RealtimeThread();
            this.connectThread.start();
        } else {
            Log.d("cma", "연결 시도 안함");
        }

        return START_REDELIVER_INTENT;
    }

    class RealtimeThread extends Thread {
        private SocketAddress sockAddr = null;
        private Socket socket = null;

        public RealtimeThread() {
            this.sockAddr = new InetSocketAddress(ServerHost.SERVER_HOST, ServerHost.SERVER_PORT);
        }

        @Override
        public void run() {
            while(true) {
                try {
                    this.socket = new Socket();
                    this.socket.connect(this.sockAddr, ServerHost.TIME_OUT);
                    Log.d("cma", "서버와 연결 하였습니다.");

                    new NetRequester(socket);
                    new NetResponser(socket, getApplicationContext()).start();

                    isConnected = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    isConnected = false;
                }

                Intent intent = new Intent("kr.co.aperturedev.callmyadminc.onconnect");
                intent.putExtra("is-connect", isConnected);
                sendBroadcast(intent);

                if(!isConnected) {
                    Log.d("cma", "실패, 다시 연결 시도");
                    try {Thread.sleep(1000); } catch(Exception ex){}
                    continue;
                }

                return;
            }
        }
    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
