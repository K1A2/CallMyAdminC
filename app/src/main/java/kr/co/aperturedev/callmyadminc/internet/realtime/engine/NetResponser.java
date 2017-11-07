package kr.co.aperturedev.callmyadminc.internet.realtime.engine;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.DataInputStream;
import java.net.Socket;

/**
 * Created by 5252b on 2017-11-05.
 * 네트워크 응답을 받습니다.
 *
 */

public class NetResponser extends Thread {
    private Socket socket = null;
    private DataInputStream dis = null;
    private Context context = null;

    public static NetResponser responser = null;

    public NetResponser(Socket socket, Context context) {
        this.socket = socket;
        this.context = context;
        NetResponser.responser = this;
    }

    @Override
    public void run() {
        try {
            this.dis = new DataInputStream(this.socket.getInputStream());
        } catch(Exception ex) {
            ex.printStackTrace();
            disconnected();
            return;
        }

        while(true) {
            try {
                String jsonScript = this.dis.readUTF();

                if(jsonScript == null) {
                    disconnected();
                    return;
                }

                Log.d("cma", "받은 데이터 : " + jsonScript);
                received(jsonScript);
            } catch(Exception ex) {
                ex.printStackTrace();
                disconnected();
                return;
            }
        }
    }

    private void disconnected() {
        NetResponser.responser = null;
        Intent intent = new Intent("kr.co.aperturedev.callmyadminc.ondisconnect");
        this.context.sendBroadcast(intent);
    }

    private void received(String jsonScript) {
        Intent intent = new Intent("kr.co.aperturedev.callmyadminc.onreceive");
        intent.putExtra("json-script", jsonScript);
        this.context.sendBroadcast(intent);
    }

    public Socket getSocket() {
        return this.socket;
    }
}
