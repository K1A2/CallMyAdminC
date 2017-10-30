package kr.co.aperturedev.callmyadminc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jckim on 2017-10-30.
 */

public class LoginDialog extends DialogFragment {

    private Context con;
    private Button btnLogin;
    private EditText editId;
    private EditText editPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_login, container, false);
        con = root.getContext();

        btnLogin = (Button)root.findViewById(R.id.Button_login);
        editId = (EditText)root.findViewById(R.id.Edit_uerId);
        editPass = (EditText)root.findViewById(R.id.Edit_uerPass);

        btnLogin.setOnClickListener(onClickListenerLogin);

        return root;
    }

    private OnLoginClickedListener onLogininClickedListener;

    public interface OnLoginClickedListener {
        public void OnLoginClickListener(String id, String password);
    }

    View.OnClickListener onClickListenerLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String id, pass;
            id = editId.getText().toString();
            pass = editPass.getText().toString();

            if ((id.length() == 0||id.isEmpty())&&(pass.length() == 0||pass.isEmpty())) {
                Toast.makeText(con, "모두 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                onLogininClickedListener.OnLoginClickListener(id, pass);
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onLogininClickedListener = (OnLoginClickedListener) activity;
    }
}
