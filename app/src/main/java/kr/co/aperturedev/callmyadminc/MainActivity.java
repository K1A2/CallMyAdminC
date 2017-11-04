package kr.co.aperturedev.callmyadminc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.aperturedev.callmyadminc.internet.tcp.OnConnectListener;
import kr.co.aperturedev.callmyadminc.internet.tcp.RealtimeConnector;
import kr.co.aperturedev.callmyadminc.module.authme.AuthmeModule;
import kr.co.aperturedev.callmyadminc.module.authme.AuthmeObject;
import kr.co.aperturedev.callmyadminc.module.authme.OnAuthmeListener;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigKeys;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigManager;
import kr.co.aperturedev.callmyadminc.view.activitys.LoginActivity;
import kr.co.aperturedev.callmyadminc.view.custom.ProgressManager;
import kr.co.aperturedev.callmyadminc.view.custom.dialog.main.DialogManager;
import kr.co.aperturedev.callmyadminc.view.custom.dialog.main.clicklistener.OnYesClickListener;
import kr.co.aperturedev.callmyadminc.view.list.ServerListAdapter;
import kr.co.aperturedev.callmyadminc.view.list.ServerListItem;

public class MainActivity extends AppCompatActivity implements OnAuthmeListener, OnYesClickListener {
    //뷰
    private ListView listServer;
    private TextView txtName;

    //잡것(?)
    private AddServer addServer = null;
    private String userName = "";
    private ConfigManager cfgMgr;
    private ServerListItem serverListItem;
    private ServerListAdapter serverListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UUID 값 이 있는지 확인합니다.
        cfgMgr = new ConfigManager(ConfigKeys.KEY_REPOSITORY, this);
        String deviceUUID = cfgMgr.get().getString(ConfigKeys.KEY_DEVICE_UUID, null);

        if(deviceUUID == null) {
            // 로그인 화면으로 이동
            Intent login = new Intent(this, LoginActivity.class);
            startActivityForResult(login, 1000);
        } else {
            // 인증 모듈을 사용하여 장치 인증
            AuthmeModule authmeMd = new AuthmeModule(deviceUUID, this);
            authmeMd.run();
            Log.d("cma", "onCreate 에서 요청");
        }
    }

    /*
        해당 사용자가 가지고 있는 서버의 목록과 정보를
        가져와서 화면에 표시합니다.
     */
    private void onReload() {
        //닉네임=UUID라고 가정(실제론 서버에서 다 가져옴)
        txtName = (TextView)findViewById(R.id.Text_name);//일딴 닉넴표시 테스트 용도
        if (userName.length() == 0) {
            userName = cfgMgr.get().getString(ConfigKeys.KEY_DEVICE_UUID, null);
        }
        txtName.setText(userName + "님 서버 목록");

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
        });//
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1000:
                    // 로그인 작업을 시작함
                    cfgMgr = new ConfigManager(ConfigKeys.KEY_REPOSITORY, this);
                    String deviceUUID = cfgMgr.get().getString(ConfigKeys.KEY_DEVICE_UUID, null);

                    AuthmeModule authmeMd = new AuthmeModule(deviceUUID, this);
                    authmeMd.run();
                    Log.d("cma", "onActivityResult 에서 요청");
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case 1000:
                    finish();
                    break;
            }
        }
    }

    // 장치 인증 결과
    @Override
    public void onAuthme(boolean isSucc, AuthmeObject authme) {
        Log.d("cma", isSucc + "입니다.");

        if(!isSucc) {
            // 실패 시
            DialogManager dm = new DialogManager(this);
            dm.setTitle("인증 실패");
            dm.setDescription("장치 인증에 실패하였습니다!");
            dm.setOnYesButtonClickListener(this, "앱 종료");
            dm.show();

            cfgMgr = new ConfigManager(ConfigKeys.KEY_REPOSITORY, this);
            cfgMgr.put(ConfigKeys.KEY_DEVICE_UUID, null);
        } else {
            Toast.makeText(getApplicationContext(), authme.getNickname() + "님 환영합니다.", Toast.LENGTH_LONG).show();

            // 서버와 연결 합니다.
            ProgressManager pm = new ProgressManager(this);
            pm.setCancelable(false);
            pm.setMessage("Now connecting...");
            pm.enable();

            RealtimeConnector conn = new RealtimeConnector();
            conn.connect(new OnServerConnectListener(pm));
        }
    }

    class OnServerConnectListener implements OnConnectListener {
        private ProgressManager pm = null;

        public OnServerConnectListener(ProgressManager pm) {
            this.pm = pm;
        }

        @Override
        public void onConnect(boolean isConnect) {
            Log.d("cma", "리얼타인 : " + isConnect);
            this.pm.disable();
        }
    }

    @Override
    public void onClick(DialogInterface dialog) {
        dialog.dismiss();
        finish();
    }
}
