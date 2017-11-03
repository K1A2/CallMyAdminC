package kr.co.aperturedev.callmyadminc.internet.tcp;


import kr.co.aperturedev.callmyadminc.internet.tcp.packet.ResponsePacket;

public interface OnResponseListener {
	void onResponse(ResponsePacket respPacket);
}
