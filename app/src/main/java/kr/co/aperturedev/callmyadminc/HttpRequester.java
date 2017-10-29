package kr.co.aperturedev.callmyadminc;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jckim on 2017-10-29.
 */

public class HttpRequester extends Thread {
    private JSONObject jsonObj = null;
    private String reqURL = null;
    private OnHttpResponse listener = null;

    public HttpRequester(JSONObject jsonObj, String reqURL) {
        this.jsonObj = jsonObj;
        this.reqURL = reqURL;
    }

    public void setListener(OnHttpResponse listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        JSONObject resJsonObj = null;
        boolean isSucc = false;

        try {
            // 커넥션을 만듭니다.
            URL url = new URL(this.reqURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Language", "ko-KR");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            // 요청
            String parameter = "data=" + URLEncoder.encode(this.jsonObj.toString(), "EUC-KR");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(parameter);
            wr.flush();

            // 응답
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder response = new StringBuilder();
            String line = "";

            while((line = reader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            reader.close();

            Log.e("Respond", "Http response : \n " + response.toString());

            resJsonObj = new JSONObject(response.toString());
            isSucc = true;
        } catch(Exception ex) {
            Log.e("Error", "HttpRequest Error!!!");
            ex.printStackTrace();
            isSucc = false;
        }

        if(this.listener != null) {
            // 리스너가 등록된 상태
            this.listener.onResponse(isSucc, resJsonObj);
        }
    }

    public interface OnHttpResponse {
        void onResponse(boolean isSucc, JSONObject jsonObj);
    }
}
