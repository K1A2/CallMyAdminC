package kr.co.aperturedev.callmyadminc.internet.tcp;

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

    public static RealtimeConnector connector = null;   // 연결 관리자
    public static NetRequester requester = null;        // 요청 관리자
    public static NetResponser responser = null;        // 응답 관리자

    public RealtimeConnector(OnConnectListener listener) {
        this.sockAddr = new InetSocketAddress(SocketConstant.SERVER_HOST, SocketConstant.SERVER_PORT);
        RealtimeConnector.connector = this;
    }

    // 서버와 연결 합니다.
    public void connect() {

    }

    // 서버와 연결을 끊습니다.
    public void lost() {

    }

    // 서버와 다시 연결 합니다.
    public void reconnect() {

    }

    class NetworkSocketLevel extends Thread {
        private OnConnectListener listener = null;

        public NetworkSocketLevel(OnConnectListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            try {

            } catch(Exception ex) {

            }
        }
    }
}
