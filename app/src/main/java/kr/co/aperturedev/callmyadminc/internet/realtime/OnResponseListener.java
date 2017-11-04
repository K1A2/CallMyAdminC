package kr.co.aperturedev.callmyadminc.internet.realtime;

import kr.co.aperturedev.callmyadminc.internet.realtime.packet.ResponsePacket;

/**
 * Created by 5252b on 2017-11-04.
 */

public interface OnResponseListener {
    void onResponse(ResponsePacket respPacket);
}
