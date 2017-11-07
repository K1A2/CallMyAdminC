package kr.co.aperturedev.callmyadminc.module.authme;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 5252b on 2017-11-03.
 * 인증 객체
 */

public class AuthmeObject {
    private String nickname = null;     // 닉네임
    private int code = 0;               // 코드 (닉네임#코드)
    private String uuid = null;         // 서버 측 고유 번호 (장치 고유번호와 다름)

    private JSONObject returnData = null;   // 인증 문서 원본

    public AuthmeObject(JSONObject returnData) throws JSONException {
        this.nickname = returnData.getString("adm-nickname");
        this.code = returnData.getInt("adm-code");
        this.uuid = returnData.getString("adm-uuid");

        this.returnData = returnData;
    }

    public String getNickname() {
        return nickname;
    }

    public int getCode() {
        return code;
    }

    public String getUuid() {
        return uuid;
    }

    public String getObjectJSONScript() {
        return this.returnData.toString();
    }
}
