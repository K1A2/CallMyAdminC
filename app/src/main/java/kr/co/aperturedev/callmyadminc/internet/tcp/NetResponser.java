package kr.co.aperturedev.callmyadminc.internet.tcp;

import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import kr.co.aperturedev.callmyadminc.internet.tcp.packet.ResponsePacket;

/**
 * Created by 5252b on 2017-11-03.
 */

public class NetResponser extends Thread {
    private Socket socket = null;
    private DataInputStream dis = null;
    private HashMap<String, OnResponseListener> listeners = null;

    private SwitchMainThread switcher = null;

    public NetResponser(Socket socket) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.listeners = new HashMap<>();

        this.switcher = new SwitchMainThread();
    }

    @Override
    public void run() {
        try {
            String jsonScript = this.dis.readUTF();
            ResponsePacket respPacket = new ResponsePacket(jsonScript);

            Message msg = Message.obtain();
            msg.obj = respPacket;
            this.switcher.handleMessage(msg);
        } catch(SocketException sex) {
            // 서버와 연결이 종료된경우

        } catch(IOException iex) {

        } catch(Exception ex) {

        }
    }

    class SwitchMainThread extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                ResponsePacket respPacket = null;

                OnResponseListener listener = listeners.get(respPacket.getRequestCode());
            } catch(Exception ex) {

            }
        }
    }
}
