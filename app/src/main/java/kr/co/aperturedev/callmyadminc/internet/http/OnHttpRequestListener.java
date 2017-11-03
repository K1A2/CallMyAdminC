package kr.co.aperturedev.callmyadminc.internet.http;

import org.json.JSONObject;

/**
 * Created by 5252b on 2017-10-29.
 */

public interface OnHttpRequestListener {
    void onResponse(boolean isSucc, JSONObject jsonObj);
}
