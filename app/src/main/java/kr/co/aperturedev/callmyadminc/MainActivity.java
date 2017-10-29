package kr.co.aperturedev.callmyadminc;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements OnHttpRequestListener {

    //뷰
    private ListView listServer;

    //잡것(?)
    private AddServer addServer = null;
    private ServerListItem serverListItem;
    private ServerListAdapter serverListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start();

        //뷰 초기화
        listServer = (ListView)findViewById(R.id.List_server);

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
                serverListItem = (ServerListItem)serverListAdapter.getItem(pos);
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
}
