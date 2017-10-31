package kr.co.aperturedev.callmyadminc.internet.http;

import android.os.Handler;
import android.os.Message;
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
 * Created by 5252b on 2017-10-29.
 * Http 프로토콜을 통한 요청 장치
 *
 * reqeustData = 서버에 요청할때 사용될 데이터
 * url = 요청 URL
 */

public class HttpRequester extends Thread {
    private String url = null;
    private JSONObject jsonObject = null;
    private OnHttpRequestListener listener = null;

    private Handler backgroundHandler = null;

    public HttpRequester(JSONObject requestData, String url) {
        this.url = url;
        this.jsonObject = requestData;
        this.backgroundHandler = new BackgroundSwitchHandler();
    }

    public void setListener(OnHttpRequestListener listener) {
        /*
        HTTP 요청에 대한 응답값을 받을 리스너
         */
        this.listener = listener;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        JSONObject returnData = null;
        boolean isSucc = false;

        try {
            URL url = new URL(this.url);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Language", "ko-KR");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            // 요청
            String parameter = "data=" + URLEncoder.encode(this.jsonObject.toString(), "EUC-KR");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(parameter);
            wr.flush();


            // 요청
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder response = new StringBuilder();
            String line = "";

            while((line = reader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            reader.close();

            String returnScript = response.toString();
            Log.d("cma", "HTTP Response : " + returnScript);

            returnData = new JSONObject(returnScript);
            isSucc = true;
        } catch(Exception ex) {
            Log.e("cma", "HTTP Response Error : ");
            ex.printStackTrace();
            isSucc = false;
        }

        Message msg = Message.obtain();
        msg.what = isSucc ? 1 : 0;
        msg.obj = returnData;
        //

        this.backgroundHandler.sendMessage(msg);
    }

    class BackgroundSwitchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            JSONObject jsonObj = (JSONObject) msg.obj;
            listener.onRequest((msg.what == 0 ? false : true), jsonObj);
        }
    }
}
