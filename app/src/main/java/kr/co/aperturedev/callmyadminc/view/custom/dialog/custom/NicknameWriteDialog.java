package kr.co.aperturedev.callmyadminc.view.custom.dialog.custom;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kr.co.aperturedev.callmyadminc.R;

/**
 * Created by 5252b on 2017-11-02.
 * 사용자의 닉네임을 받아오는 다이얼로그
 */

public class NicknameWriteDialog {
    private Activity context = null;
    private Dialog dialog = null;

    private EditText nicknameEditor = null; // 닉네임 입력 에디터
    private Button okButton = null;         // 확인 버튼

    public NicknameWriteDialog(Activity context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }

    public void build() {
        this.dialog.setTitle("닉네임 입력");
        this.dialog.setContentView(R.layout.dialog_name);

        this.nicknameEditor = (EditText) this.dialog.findViewById(R.id.nickname_editor);
        this.okButton = (Button) this.dialog.findViewById(R.id.dialog_nickname_buttons_ok);
        this.dialog.setCancelable(false);   // 닫힘 방지
    }

    public void setEventHandler(View.OnClickListener clickListener) {
        this.okButton.setOnClickListener(clickListener);
    }

    public Editable getNickname() {
        return this.nicknameEditor.getText();
    }

    public void show() {
        this.dialog.show();
    }

    public void disable() {
        this.dialog.dismiss();
    }
}
