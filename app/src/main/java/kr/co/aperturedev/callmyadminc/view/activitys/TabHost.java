package kr.co.aperturedev.callmyadminc.view.activitys;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import kr.co.aperturedev.callmyadminc.R;
import kr.co.aperturedev.callmyadminc.TabPagerAdapter;
import kr.co.aperturedev.callmyadminc.internet.realtime.OnConnectListener;
import kr.co.aperturedev.callmyadminc.internet.realtime.RealtimeService;
import kr.co.aperturedev.callmyadminc.internet.realtime.broadcaster.OnConnectBroadcast;
import kr.co.aperturedev.callmyadminc.module.authme.AuthmeModule;
import kr.co.aperturedev.callmyadminc.module.authme.AuthmeObject;
import kr.co.aperturedev.callmyadminc.module.authme.OnAuthmeListener;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigKeys;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigManager;
import kr.co.aperturedev.callmyadminc.view.custom.ProgressManager;
import kr.co.aperturedev.callmyadminc.view.custom.dialog.main.DialogManager;
import kr.co.aperturedev.callmyadminc.view.custom.dialog.main.clicklistener.OnYesClickListener;

public class TabHost extends AppCompatActivity implements OnAuthmeListener, OnYesClickListener, OnConnectListener {
    //뷰
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //잡것(?)
    private ConfigManager cfgMgr;
    private ProgressManager pm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabhost);

        // 리얼타임 서비스를 실행합니다.
        boolean isRunning = false;
        ActivityManager mgr = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : mgr.getRunningServices(Integer.MAX_VALUE)) {
            if("kr.co.aperturedev.callmyadminc.internet.realtime.RealtimeService".equals(service.service.getClassName())) {
                isRunning = true;
            }
        }

        if(!isRunning) {
            // 실행 시킨다.
            this.pm = new ProgressManager(this);
            this.pm.setMessage("Now connecting...");
            this.pm.setCancelable(false);
            this.pm.enable();

            OnConnectBroadcast.clearListener();
            OnConnectBroadcast.addListener(this);

            Intent connector = new Intent(this, RealtimeService.class);
            startService(connector);
        }


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

    @Override
    protected void onResume() {
        super.onResume();

        tabLayout = (TabLayout)findViewById(R.id.TabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("서버목록"));
        tabLayout.addTab(tabLayout.newTab().setText("서버추가"));
        tabLayout.addTab(tabLayout.newTab().setText("앱상태"));
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
        }

        // 인증 값을 어따 저장 해야하는데 말이지..
    }

    @Override
    public void onClick(DialogInterface dialog) {
        dialog.dismiss();
        finish();
    }

    @Override
    public void onConnect(boolean isConnect) {
        this.pm.disable();
        OnConnectBroadcast.clearListener();

        DialogManager dm = new DialogManager(this);
        dm.setTitle("네트워크 오류");
        dm.setDescription("서버가 응답하지 않습니다.\n네트워크 상태를 확인하세요.");
        dm.setOnYesButtonClickListener(new OnYesClickListener() {
            @Override
            public void onClick(DialogInterface dialog) {
                dialog.dismiss();
                finish();
            }
        }, "앱 종료");

        if(!isConnect) {
            dm.show();
            return;
        }
    }
}
