package kr.co.aperturedev.callmyadminc.internet.realtime.listeners;

import kr.co.aperturedev.callmyadminc.module.authme.AuthmeObject;

/**
 * Created by 5252b on 2017-11-06.
 */

public interface OnAuthmeListener {
    void onAuthme(boolean isSucc, AuthmeObject authmeObj);
}
