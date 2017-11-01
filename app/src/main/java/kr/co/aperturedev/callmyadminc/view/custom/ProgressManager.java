package kr.co.aperturedev.callmyadminc.view.custom;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by 5252b on 2017-07-04.
 * Progress Dialog 를 띄워주는 클래스
 */

public class ProgressManager {
    private ProgressDialog dialog = null;

    public ProgressManager(Context context) {
        this.dialog = new ProgressDialog(context);
        this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void setMessage(String msg) {
        this.dialog.setMessage(msg);
    }

    public void setCancelable(boolean b) {
        this.dialog.setCancelable(b);
    }

    public void enable(){
        this.dialog.show();
    }

    public void disable() {
        this.dialog.dismiss();
    }
}
