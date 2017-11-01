package kr.co.aperturedev.callmyadminc.view.activitys;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import kr.co.aperturedev.callmyadminc.R;
import kr.co.aperturedev.callmyadminc.core.context.APIKeys;

/**
 * Created by 5252b on 2017-10-31.
 *
 */

public class LoginActivity extends AppCompatActivity {
    private OAuthLogin naverLogin = null;           // 네이버 로그인 객체
    private OAuthLoginButton naverLoginButton = null;   // 네이버 로그인 버튼

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.naverLogin = OAuthLogin.getInstance();
        this.naverLogin.init(
                this,
                APIKeys.NAVER_CLIENT_ID,
                APIKeys.NAVER_CLIENT_SEC,
                "CallMyAdmin"
        );

        this.naverLoginButton = (OAuthLoginButton) findViewById(R.id.naver_login_button);
        this.naverLoginButton.setOAuthLoginHandler(new OnNaverLoginHandler(this));
    }

    class OnNaverLoginHandler extends OAuthLoginHandler {
        private Context context = null;

        public OnNaverLoginHandler(Context context) {
            this.context = context;
        }

        @Override
        public void run(boolean b) {
            if(b) {
                String accessToken = naverLogin.getAccessToken(this.context);

                ReadUserdataThread reader = new ReadUserdataThread();
                reader.execute(accessToken);
            } else {
                Log.e("cma", "로그인에 실패하였습니다.");
            }
        }

        private class ReadUserdataThread extends AsyncTask<String, Void, String> {
            @Override
            protected void onPostExecute(String s) {
                // 여기서 액티비티를 끝내고 값을 리턴합니다.
                Log.d("cma", "로그인 성공!!!");
                Log.d("cma", s);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected String doInBackground(String... strings) {
                String accessToken = strings[0];

                String response = naverLogin.requestApi(context, accessToken, "https://openapi.naver.com/v1/nid/me");
                return response;
            }
        }
    }
}
