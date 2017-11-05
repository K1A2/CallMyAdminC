package kr.co.aperturedev.callmyadminc.internet.realtime.engine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by 5252b on 2017-11-05.
 */

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
            NetRequester.requester = null;
            return false;
        }
    }
}
