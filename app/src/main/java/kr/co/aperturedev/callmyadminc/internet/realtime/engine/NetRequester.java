package kr.co.aperturedev.callmyadminc.internet.realtime.engine;

import android.os.Handler;
import android.os.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import kr.co.aperturedev.callmyadminc.internet.realtime.listeners.OnRequestListener;

/**
 * Created by 5252b on 2017-11-05.
 */

public class NetRequester extends Thread {
    private Socket socket = null;
    private String jsonScript = null;
    private DataOutputStream dos = null;
    private SwitchBackgroundHandler handler = null;
    private OnRequestListener listener = null;

    public NetRequester(Socket socket, OnRequestListener listener, String jsonScript) throws IOException {
        this.socket = socket;
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.jsonScript = jsonScript;
        this.handler = new SwitchBackgroundHandler();
        this.listener = listener;
    }

    @Override
    public void run() {
        Message msg = Message.obtain();

        try {
            this.dos.writeUTF(jsonScript);
            msg.what = 1;
        } catch(Exception ex) {
            ex.printStackTrace();
            msg.what = 0;
        }

        this.handler.sendMessage(msg);
    }

    class SwitchBackgroundHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(listener != null) {
                listener.onRequest(msg.what == 0 ? false : true);
            }
        }
    }
}
