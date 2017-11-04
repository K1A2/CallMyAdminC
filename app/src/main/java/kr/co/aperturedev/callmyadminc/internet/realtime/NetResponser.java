package kr.co.aperturedev.callmyadminc.internet.realtime;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

/**
 * Created by 5252b on 2017-11-04.
 */

public class NetResponser extends Thread {
    private Socket socket = null;
    private DataInputStream dis = null;
    private HashMap<String, OnResponseListener> listeners = null;

    public static NetResponser responser = null;

    public NetResponser(Socket socket) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.listeners = new HashMap<>();

        NetResponser.responser = this;
    }

    @Override
    public void run() {
        while(true) {
            try {
                String jsonScript = this.dis.readUTF();
                Log.e("cma", "응답 성공!!\n" + jsonScript);
//                ResponsePacket respPacket = new ResponsePacket(jsonScript);
            } catch(SocketException ex) {
                // 서버와 연결 끊김
            } catch(Exception ex) {
                // 통신 오류
                continue;
            }
        }
    }

    public void addListener(String reqCode, OnResponseListener listener) {
        this.listeners.put(reqCode, listener);
    }
}
