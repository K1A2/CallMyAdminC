package kr.co.aperturedev.callmyadminc.view.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import kr.co.aperturedev.callmyadminc.R;
import kr.co.aperturedev.callmyadminc.TabPagerAdapter;
import kr.co.aperturedev.callmyadminc.internet.realtime.RealtimeService;
import kr.co.aperturedev.callmyadminc.module.authme.AuthmeModule;
import kr.co.aperturedev.callmyadminc.module.authme.AuthmeObject;
import kr.co.aperturedev.callmyadminc.module.authme.OnAuthmeListener;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigKeys;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigManager;
import kr.co.aperturedev.callmyadminc.view.custom.dialog.main.DialogManager;
import kr.co.aperturedev.callmyadminc.view.custom.dialog.main.clicklistener.OnYesClickListener;

public class TabHost extends AppCompatActivity implements OnAuthmeListener, OnYesClickListener {
    //뷰
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //잡것(?)
    private ConfigManager cfgMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabhost);

        // UUID 값 이 있는지 확인합니다.
        cfgMgr = new ConfigManager(ConfigKeys.KEY_REPOSITORY, this);
        String deviceUUID = cfgMgr.get().getString(ConfigKeys.KEY_DEVICE_UUID, null);

        getSupportActionBar().hide();

        if(deviceUUID == null) {
            // 로그인 화면으로 이동
            Intent login = new Intent(this, LoginActivity.class);
            startActivityForResult(login, 1000);
        } else {
            // 인증 모듈을 사용하여 장치 인증
            AuthmeModule authmeMd = new AuthmeModule(deviceUUID, this);
            authmeMd.run();
        }
    }

    //액션바 메뉴생성
    /*@Override
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
    }*/

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

            tabLayout = (TabLayout)findViewById(R.id.TabLayout);
            tabLayout.addTab(tabLayout.newTab().setText("서버목록"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            viewPager = (ViewPager)findViewById(R.id.ViewPager);

            TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(tabPagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            Toast.makeText(getApplicationContext(), authme.getNickname() + "님 환영합니다.", Toast.LENGTH_LONG).show();

            Intent connector = new Intent(this, RealtimeService.class);
            startService(connector);
        }
    }

    @Override
    public void onClick(DialogInterface dialog) {
        dialog.dismiss();
        finish();
    }
}
