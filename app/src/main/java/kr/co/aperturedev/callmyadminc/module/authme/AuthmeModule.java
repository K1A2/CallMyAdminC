package kr.co.aperturedev.callmyadminc.module.authme;

import org.json.JSONObject;

import kr.co.aperturedev.callmyadminc.internet.http.HttpRequester;
import kr.co.aperturedev.callmyadminc.internet.http.OnHttpRequestListener;
import kr.co.aperturedev.callmyadminc.internet.http.RequestURLS;

/**
 * Created by 5252b on 2017-11-03.
 */

public class AuthmeModule implements OnHttpRequestListener {
    private String uuid = null;
    private OnAuthmeListener listener = null;

    public AuthmeModule(String uuid, OnAuthmeListener listener) {
        this.uuid = uuid;
        this.listener = listener;
    }

    public void run() {
        // uuid 값을 통해 인증 값을 가져옵니다.
        try {
            JSONObject requestObj = new JSONObject();
            requestObj.put("device-uuid", this.uuid);

            HttpRequester requester = new HttpRequester(requestObj, RequestURLS.DEVICE_AUTHME);
            requester.setListener(this);
            requester.start();
        } catch(Exception ex) {
            this.listener.onAuthme(false, null);
        }
    }

    @Override
    public void onResponse(boolean isSucc, JSONObject jsonObj) {
        if(!isSucc) {
            this.listener.onAuthme(false, null);
            return;
        }

        try {
            boolean isLoginSucc = jsonObj.getBoolean("is-success");

            if(!isLoginSucc) {
                // 로그인에 실패헀을 경우
                this.listener.onAuthme(false, null);
            }

            // 로그인 성공 시
            this.listener.onAuthme(true, new AuthmeObject(jsonObj));
        } catch(Exception ex) {
            // 오류 발생 시
            this.listener.onAuthme(false, null);
        }
    }
}
