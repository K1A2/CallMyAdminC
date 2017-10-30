package kr.co.aperturedev.callmyadminc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by jckim on 2017-10-30.
 */

public class LoginDialog extends DialogFragment {

    private Button btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_login, container, false);

        btnLogin = (Button)root.findViewById(R.id.Button_login);

        btnLogin.setOnClickListener(onClickListenerLogin);

        return root;
    }

    private OnLoginClickedListener onLogininClickedListener;

    public interface OnLoginClickedListener {
        public void OnLoginClickListener();
    }

    View.OnClickListener onClickListenerLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onLogininClickedListener.OnLoginClickListener();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onLogininClickedListener = (OnLoginClickedListener) activity;
    }
}
