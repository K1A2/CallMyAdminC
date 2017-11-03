package kr.co.aperturedev.callmyadminc.internet.tcp.packet;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponsePacket {
	private JSONObject jsonObj = null;
	
	public ResponsePacket(String jsonScript) throws JSONException {
		this.jsonObj = new JSONObject(jsonScript);
	}
	
	public String getHeader() throws JSONException {
		return (String) this.jsonObj.get("server-header-type");
	}
	
	public String getRequestCode() throws JSONException {
		return (String) this.jsonObj.get("server-header-requestcode");
	}
	
	public long getRequestTime() throws JSONException {
		return (long) this.jsonObj.get("server-header-requesttime");
	}
	
	public String getMessage() throws JSONException {
		return (String) this.jsonObj.get("server-body-message");
	}
	
	public JSONArray getArgs() throws JSONException {
		return (JSONArray) this.jsonObj.get("server-body-args");
	}
	
	
	public int getResultCode() throws JSONException {
		return (int) this.jsonObj.get("server-footer-resultcode");
	}
}
