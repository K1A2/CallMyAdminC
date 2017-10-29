package kr.co.aperturedev.callmyadminc;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AddServer addServer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start();

        //액션바
        ActionBar actionBar = getSupportActionBar();
    }

    private void start() {
        //서버연결
        try {
            JSONObject a = new JSONObject();
            a.put("Hello", "World!");

            HttpRequester httpRequester = new HttpRequester(a, "http://aperturedev.co.kr/callmyadmin/create/server-regi.jsp");
            httpRequester.start();
        } catch(Exception ex){}
    }

    //액션바 메뉴생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
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
                addServer.show(fragmentManager, "ssss");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
