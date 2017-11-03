package kr.co.aperturedev.callmyadminc.view.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

import kr.co.aperturedev.callmyadminc.R;
import kr.co.aperturedev.callmyadminc.core.context.APIKeys;
import kr.co.aperturedev.callmyadminc.internet.http.HttpRequester;
import kr.co.aperturedev.callmyadminc.internet.http.OnHttpRequestListener;
import kr.co.aperturedev.callmyadminc.internet.http.RequestURLS;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigKeys;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigManager;
import kr.co.aperturedev.callmyadminc.view.custom.ProgressManager;
import kr.co.aperturedev.callmyadminc.view.custom.dialog.custom.NicknameWriteDialog;

/**
 * Created by 5252b on 2017-10-31.
 *
 */

public class LoginActivity extends AppCompatActivity {
    private OAuthLogin naverLogin = null;           // 네이버 로그인 객체
    private OAuthLoginButton naverLoginButton = null;   // 네이버 로그인 버튼
    private long backKeyPress;//뒤로가기버튼 시간측정
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        i = getIntent();
    }

    @Override
    public void onBackPressed() {
        Toast toast = new Toast(LoginActivity.this);
        if (System.currentTimeMillis() - backKeyPress < 2000) {
            setResult(RESULT_CANCELED, i);
            finish();
        } else {
            toast.makeText(LoginActivity.this, "어플리케이션을 종료하려면 한번 더 누르세요.", Toast.LENGTH_LONG).show();
            backKeyPress = System.currentTimeMillis();
        }
    }

    class OnNaverLoginHandler extends OAuthLoginHandler {
        private Activity context = null;

        public OnNaverLoginHandler(Activity context) {
            this.context = context;
        }

        @Override
        public void run(boolean b) {
            if(b) {
                String accessToken = naverLogin.getAccessToken(this.context);

                ReadUserdataThread reader = new ReadUserdataThread();
                reader.execute(accessToken);
            } else {
                Toast.makeText(LoginActivity.this, "로그인 처리 실패 -1", Toast.LENGTH_LONG).show();
            }
        }

        private class ReadUserdataThread extends AsyncTask<String, Void, String> implements View.OnClickListener, OnHttpRequestListener {
            private NicknameWriteDialog nickWriteDialog = null;
            private String naverUUID = null;

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

            @Override
            protected void onPostExecute(String response) {
                // 해당 UUID 값이 이미 가입된 계정인지 확인합니다.
                try {
                    JSONObject respJSONObj = new JSONObject(response);
                    String uuid = ((JSONObject) respJSONObj.get("response")).getString("enc_id");
                    this.naverUUID = uuid;  // 전역으로 올려줌

                    // 서버에 요청 합니다.
                    JSONObject requestJSON = new JSONObject();
                    requestJSON.put("target-uuid", uuid);

                    HttpRequester requester = new HttpRequester(requestJSON, RequestURLS.DEVICE_CHECK_REGIST);
                    requester.setListener(this);
                    requester.start();
                } catch(Exception ex) {
                    // 오류 발생시 출력 후 종료
                    Toast.makeText(LoginActivity.this, "응답 처리 에러 -1", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.dialog_nickname_buttons_ok) {
                    // 확인 버튼을 눌렀을 경우
                    String nickname = this.nickWriteDialog.getNickname().toString();
                    if(nickname.length() == 0) {
                        Toast.makeText(LoginActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        this.nickWriteDialog.disable();

                        // HTTP 요청을 통해 장치를 가입시킵니다.
                        try {
                            JSONObject registJSONObj = new JSONObject();
                            registJSONObj.put("user-nickname", nickname);
                            registJSONObj.put("device-uuid", naverUUID);

                            // Progress 를 띄웁니다.
                            ProgressManager pm = new ProgressManager(context);
                            pm.setMessage("Now loading...");
                            pm.setCancelable(false);
                            pm.enable();

                            HttpRequester registRequester = new HttpRequester(registJSONObj, RequestURLS.DEVICE_REGIST);
                            registRequester.setListener(new OnDeviceResgistListener(pm, nickname));
                            registRequester.start();
                        } catch(Exception ex) {
                            Toast.makeText(context, "엔진 오류가 발생하였습니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }
            }

            class OnDeviceResgistListener implements OnHttpRequestListener {
                private ProgressManager pm = null;
                private String nick = null;

                public OnDeviceResgistListener(ProgressManager pm, String nickname) {
                    this.pm = pm;
                    this.nick = nickname;
                }

                @Override
                public void onResponse(boolean isSucc, JSONObject jsonObj) {
                    this.pm.disable();

                    try {
                        if (!isSucc || jsonObj.getString("client-uuid") == null) {
                            Toast.makeText(context, "HTTP 요청 실패로 가입 할 수 없습니다.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch(Exception ex) {
                        Toast.makeText(context, "응답 에러 500", Toast.LENGTH_LONG).show();
                        return;
                    }


                    loginSuccess(naverUUID, this.nick);
                }
            }

            @Override
            public void onResponse(boolean isSucc, JSONObject jsonObj) {
                if(!isSucc) {
                    // 요청 실패시 실행 중지
                    Toast.makeText(LoginActivity.this, "HTTP 요청 Fail!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    if (!jsonObj.getBoolean("is-regist")) {
                        // 가입되지 않은 계정
                        openNicknameWriteDialog();
                    } else {
                        // 가입된 게정
                        String nickname = jsonObj.getString("admin-nickname");
                        loginSuccess(this.naverUUID, nickname);
                    }
                } catch(Exception ex) {
                    // 오류 발생시 강행
                    openNicknameWriteDialog();
                }
            }

            private void openNicknameWriteDialog() {
                this.nickWriteDialog = new NicknameWriteDialog(context);
                this.nickWriteDialog.build();     // 다이얼로그 생성
                this.nickWriteDialog.setEventHandler(this);
                this.nickWriteDialog.show();
            }

            private void loginSuccess(String uuid, String nickname) {
                // UUID 값 저장
                ConfigManager cfgMgr = new ConfigManager(ConfigKeys.KEY_REPOSITORY, LoginActivity.this);
                cfgMgr.put(ConfigKeys.KEY_DEVICE_UUID, uuid);

                // 액티비티 종료
                setResult(RESULT_OK, i);
                finish();
            }
        }
    }
}
