package kr.co.aperturedev.callmyadminc.internet.tcp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by 5252b on 2017-11-03.
 * (c) 2017 Aperture software.
 * 실시간 서버와 접속 합니다.
 */

public class RealtimeConnector {
    private SocketAddress sockAddr = null;
    private Socket socket = null;
    private NetworkSocketLevel socketLv = null;

    private OnConnectListener listener = null;
    private SwitchMainThread switcher = null;

    public static RealtimeConnector connector = null;   // 연결 관리자
    public static NetRequester requester = null;        // 요청 관리자
    public static NetResponser responser = null;        // 응답 관리자

    public RealtimeConnector(String a) {
        this.sockAddr = new InetSocketAddress(SocketConstant.SERVER_HOST, SocketConstant.SERVER_PORT);
        this.switcher = new SwitchMainThread();
        RealtimeConnector.connector = this;
    }

    // 서버와 연결 합니다.
    public void connect(OnConnectListener listener) {
        this.socketLv = new NetworkSocketLevel();
        this.socketLv.start();
        this.listener = listener;
    }

    // 서버와 연결을 끊습니다.
    public void lost() {
        try {
            this.socket.close();
            if(this.socketLv != null) {
                this.socketLv = null;
            }

            RealtimeConnector.requester = null;
            RealtimeConnector.responser = null;
        } catch(Exception ex) {}
    }

    // 서버와 다시 연결 합니다.
    public void reconnect(OnConnectListener listener) {
        lost();
        connect(listener);
    }

    class NetworkSocketLevel extends Thread {
        @Override
        public void run() {
            socket = new Socket();
            Looper.prepare();

            try {
                socket.connect(sockAddr, SocketConstant.TIME_OUT);

                RealtimeConnector.requester = new NetRequester(socket);
                RealtimeConnector.responser = new NetResponser(socket);
                RealtimeConnector.responser.start();

                Message msg = Message.obtain();
                msg.what = 1;
                switcher.handleMessage(msg);
            } catch (Exception ex) {
                Message msg = Message.obtain();
                msg.what = 0;
                switcher.handleMessage(msg);
            }
        }
    }

    class SwitchMainThread extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(listener != null) {
                listener.onConnect(msg.what == 0 ? false : true);
            }
        }
    }
}
