package kr.co.aperturedev.callmyadminc.view.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;

/**
 * Created by jckim on 2017-11-01.
 */

public class InputDialogManager {

    private AlertDialog.Builder dialog = null;
    private EditText inputId;

    public InputDialogManager(Context context, String title, String hint) {
        this.dialog = new AlertDialog.Builder(context);
        this.dialog.setTitle(title);

        this.inputId = new EditText(context);
        this.inputId.setHint(hint);

        this.dialog.setView(this.inputId);
    }
}
