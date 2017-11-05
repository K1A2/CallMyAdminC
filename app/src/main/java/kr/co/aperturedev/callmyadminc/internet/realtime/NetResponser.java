package kr.co.aperturedev.callmyadminc.internet.realtime;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by 5252b on 2017-11-04.
 */

public class NetResponser extends Thread {
    private Socket socket = null;
    private DataInputStream dis = null;
    private Context context = null;

    public static NetResponser responser = null;


    public NetResponser(Socket socket, Context context) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.context = context;

        NetResponser.responser = this;
    }

    @Override
    public void run() {
        while(true) {
            try {
                String jsonScript = this.dis.readUTF();

                if(jsonScript == null) {
                    doReconnect();
                    responser = null;
                    return;
                }
            } catch(Exception ex) {
                // 통신 오류
                doReconnect();
                responser = null;
                return;
            }
        }
    }

    private void doReconnect() {
        Log.d("cma", "서버와 연결이 끊어졌습니다.");

        if(this.context != null) {
            Intent connector = new Intent(this.context, RealtimeService.class);
            this.context.startService(connector);
        }
    }
}
