package kr.co.aperturedev.callmyadminc.view.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

import kr.co.aperturedev.callmyadminc.R;
import kr.co.aperturedev.callmyadminc.internet.realtime.broadcaster.OnResponseBroadcast;
import kr.co.aperturedev.callmyadminc.internet.realtime.engine.NetRequester;
import kr.co.aperturedev.callmyadminc.internet.realtime.engine.NetResponser;
import kr.co.aperturedev.callmyadminc.internet.realtime.listeners.OnResponseListener;
import kr.co.aperturedev.callmyadminc.internet.realtime.packet.RequestPacket;
import kr.co.aperturedev.callmyadminc.internet.realtime.packet.ResponsePacket;

/**
 * Created by jckim on 2017-11-05.
 */

public class AppStatusActivity extends Fragment {
    private TextView pingTestResultView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_status, container, false);
        this.pingTestResultView = (TextView) root.findViewById(R.id.status_ping_result);
        pingTest(); // 핑 계산

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void pingTest() {
        try {
            String reqId = UUID.randomUUID().toString();

            RequestPacket reqPacket = new RequestPacket("ping-test");
            reqPacket.setRequestCode(reqId);
            reqPacket.setHeader("client-request");

            OnResponseBroadcast.registerListener(reqId, new PingTestListener());
            NetRequester req = new NetRequester(NetResponser.responser.getSocket(), null, reqPacket.getJSONScript());
            req.start();
        } catch(Exception ex) {
            pingTestResultView.setText("오류가 발생하였습니다.");
        }
    }

    class PingTestListener implements OnResponseListener {
        @Override
        public void onResponse(ResponsePacket respPacket) {
            try {
                long reqTime = respPacket.getRequestTime();
                long delay = System.currentTimeMillis() - reqTime;
                pingTestResultView.setText(delay + "ms");
            } catch(Exception ex) {
                pingTestResultView.setText("오류가 발생하였습니다.");
            }
        }
    }
}
