package kr.co.aperturedev.callmyadminc.internet.realtime.packet;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestPacket {
	private JSONObject jsonObj = null;
	
	private JSONArray args = null;
	private String header = null;
	private String requestCode = null;
	
	public RequestPacket(String command) throws JSONException {
		this.jsonObj = new JSONObject();
		this.jsonObj.put("client-body-command", command);
	}
	
	public void setArgs(Object[] args) {
		this.args = new JSONArray();
		
		for(Object obj : args) {
			this.args.put(obj);
		}
	}
	
	public void setRequestCode(String code) {
		this.requestCode = code;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getJSONScript() throws JSONException {
		this.jsonObj.put("client-header-type", this.header);
		this.jsonObj.put("client-body-args", this.args);
		this.jsonObj.put("client-header-requestcode", this.requestCode);
		this.jsonObj.put("client-footer-requesttime", System.currentTimeMillis());
		
		return this.jsonObj.toString();
	}
}
