package kr.co.aperturedev.callmyadminc.view.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import kr.co.aperturedev.callmyadminc.R;
import kr.co.aperturedev.callmyadminc.core.context.APIKeys;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigKeys;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigManager;

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
            toast.makeText(LoginActivity.this, "로그인을 안하면 앱 사용이 불가합니다. 한번더 클릭면 앱이 종료됩니다.", Toast.LENGTH_LONG).show();
            backKeyPress = System.currentTimeMillis();
        }
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
            protected void onPostExecute(String s) {
                // 여기서 액티비티를 끝내고 값을 리턴합니다.
                Log.d("cma", "로그인 성공!!!");
                Log.d("cma", s);

                //xml파일 전개
                final LinearLayout linearLayout = (LinearLayout)View.inflate(LoginActivity.this, R.layout.dialog_name, null);
                final EditText editId = (EditText)linearLayout.findViewById(R.id.Edit_nickname);

                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                dialog.setTitle("닉네임 입력");
                dialog.setView(linearLayout);//다이얼로그 뷰 설정
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //다이얼로그 버튼 리스너 커스텀
                final AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = editId.getText().toString();
                        if (name.isEmpty()||name.length() == 0) {
                            Toast.makeText(LoginActivity.this, "입력해주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            alertDialog.dismiss();
                            //UUID를 닉네임으로 가정
                            ConfigManager cfgMgr = new ConfigManager(ConfigKeys.KEY_REPOSITORY, LoginActivity.this);
                            cfgMgr.put(ConfigKeys.KEY_DEVICE_UUID, name);

                            //닉넴 메인으로 넘겨줌 (나중에는 서버가 처리할부분)
                            i.putExtra("Name", name);
                            setResult(RESULT_OK, i);
                            finish();
                        }
                    }
                });
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LoginActivity.this, "앱을강제종료 합니다", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        setResult(RESULT_CANCELED, i);
                        finish();
                    }
                });
            }
        }
    }
}
