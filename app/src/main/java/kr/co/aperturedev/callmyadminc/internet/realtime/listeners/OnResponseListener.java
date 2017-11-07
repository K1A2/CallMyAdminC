package kr.co.aperturedev.callmyadminc.internet.realtime.listeners;

import kr.co.aperturedev.callmyadminc.internet.realtime.packet.ResponsePacket;

/**
 * Created by 5252b on 2017-11-06.
 */

public interface OnResponseListener {
    void onResponse(ResponsePacket respPacket);
}
