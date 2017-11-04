package kr.co.aperturedev.callmyadminc.internet.realtime;

/*
 * (c) 2017 Aperture software.
 * 데이터 송신 처리기
 */

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetRequester {
    private Socket socket = null;

    private DataOutputStream dos = null;
    public static NetRequester requester = null;

    public NetRequester(Socket socket) throws IOException {
        this.socket = socket;

        this.dos = new DataOutputStream(socket.getOutputStream());
        NetRequester.requester = this;
    }

    public boolean send(String jsonScript) {
        try {
            this.dos.writeUTF(jsonScript);
            return true;
        } catch(Exception ex) {
            return false;
        }
    }
}
