package kr.co.aperturedev.callmyadminc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import kr.co.aperturedev.callmyadminc.internet.http.HttpRequester;
import kr.co.aperturedev.callmyadminc.internet.http.OnHttpRequestListener;
import kr.co.aperturedev.callmyadminc.internet.http.RequestURLS;
import kr.co.aperturedev.callmyadminc.view.list.ServerListAdapter;
import kr.co.aperturedev.callmyadminc.view.list.ServerListItem;

public class MainActivity extends AppCompatActivity implements OnHttpRequestListener, LoginDialog.OnLoginClickedListener {

    //뷰
    private ListView listServer;

    //잡것(?)
    private AddServer addServer = null;
    private String uuid;
    private FragmentManager fragmentManager;
    private LoginDialog loginDialog;
    private SharedPreferences prefUuid = null;
    private ServerListItem serverListItem;
    private ServerListAdapter serverListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UUID존재 검사
        prefUuid = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = prefUuid.getString("UUID", null);

        if (uuid == null) {//UUID 없으면 하는일
            //폰 고유 번호를 가져오기위한 권한 요청
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int permisionRequest = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE);

                if (permisionRequest == PackageManager.PERMISSION_DENIED)
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                        AlertDialog.Builder permissioCheck = new AlertDialog.Builder(MainActivity.this);
                        permissioCheck.setTitle("권한요청")
                                .setMessage("사용자 구분을 위한 장치 고유의 번호를 얻기위해 권한이 필요합니다. 만약 거부한다면, 당신은 이 앱을 사용할 수 가 없습니다.")
                                .setCancelable(false)
                                .setPositiveButton("동의", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                                        }
                                    }
                                })
                                .setNegativeButton("거부", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "앱이 종료됩니다", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
                    }
                else {
                    setUuid();
                }
            } else {
                setUuid();
            }
        } else {//UUID있음 바로 메인으로감
            main();
        }
    }

    //권한 허가했을시 처리
    @SuppressLint("MissingPermission")
    private void setUuid() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        uuid = telephonyManager.getDeviceId();

        //로그인창 띄움
        fragmentManager = getSupportFragmentManager();
        loginDialog = new LoginDialog();
        loginDialog.setCancelable(false);
        loginDialog.show(fragmentManager, "login");
    }

    //모든처리
    private void main() {
        //디버그용
        Toast.makeText(MainActivity.this, uuid, Toast.LENGTH_SHORT).show();

        //HTTP에 요청
        start();

        //뷰 초기화
        listServer = (ListView) findViewById(R.id.List_server);

        //서버리스트뷰
        serverListAdapter = new ServerListAdapter();

        //태스트용으로 집어넣음
        serverListAdapter.addItem("TestServer", "Admin: K1A2", "People: 123456789000000");
        serverListAdapter.addItem("TestServer2222", "Admin: ...?", "People: 0");

        listServer.setAdapter(serverListAdapter);

        //리스트뷰 롱클릭 처리
        listServer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                serverListItem = (ServerListItem) serverListAdapter.getItem(pos);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(String.format("\'%s\'를 목록에서 지우시겠습니까?", serverListItem.getSvName()));
                alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        serverListAdapter.remove(pos);
                        Toast.makeText(MainActivity.this, String.format("\'%s\'를 목록에서 지웠습니다", serverListItem.getSvName()), Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("취소", null);
                alert.show();
                return true;
            }
        });
    }

    private void start() {
        //서버연결
        try {
            HttpRequester reqtu
                     = new HttpRequester(null, RequestURLS.DEVICE_REGIST);
            reqtu.setListener(this);
            reqtu.start();
        } catch(Exception ex){}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUuid();
                } else {
                    Toast.makeText(MainActivity.this, "앱이 종료됩니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
        }
    }

    //액션바 메뉴생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //액션바 아이템 클릭 리스너
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_addsv:
                //디버그용 토스트
                Toast.makeText(this, "서버추가 클릭", Toast.LENGTH_SHORT).show();

                //서버 추가하는 프레그먼트 다이얼로그 띄움
               FragmentManager fragmentManager = getSupportFragmentManager();
                addServer = new AddServer();
                addServer.show(fragmentManager, "addServer");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequest(boolean isSucc, JSONObject jsonObj) {

    }

    //로그인 창에서 로그인 버튼 클릭시
    @Override
    public void OnLoginClickListener(String id, String password) {
        if (loginDialog != null) {
            loginDialog.dismiss();

            //디버그용 UUID, 로그인은 무조건 성공했다고 가정.
            //실제 실패시에는 그에맞는 처리가 필요
            prefUuid.edit().putString("UUID", uuid).commit();
            main();
        } else {//시류ㅐ했을시 로그인창 다시띄워버리기
            loginDialog.show(fragmentManager, "login");
        }
    }
}
