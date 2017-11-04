package kr.co.aperturedev.callmyadminc.view.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.aperturedev.callmyadminc.AddServer;
import kr.co.aperturedev.callmyadminc.R;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigKeys;
import kr.co.aperturedev.callmyadminc.module.configure.ConfigManager;
import kr.co.aperturedev.callmyadminc.view.list.ServerListAdapter;
import kr.co.aperturedev.callmyadminc.view.list.ServerListItem;

public class MainActivity extends Fragment {
    //뷰
    private View root;
    private ListView listServer;
    private TextView txtName;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //잡것(?)
    private AddServer addServer = null;
    private String userName = "";
    private ConfigManager cfgMgr;
    private ServerListItem serverListItem;
    private ServerListAdapter serverListAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_main, container, false);

        return root;
    }

    /*
        해당 사용자가 가지고 있는 서버의 목록과 정보를
        가져와서 화면에 표시합니다.
     */
    private void onReload() {
        //닉네임=UUID라고 가정(실제론 서버에서 다 가져옴)
        txtName = (TextView)root.findViewById(R.id.Text_name);//일딴 닉넴표시 테스트 용도
        if (userName.length() == 0) {
            userName = cfgMgr.get().getString(ConfigKeys.KEY_DEVICE_UUID, null);
        }
        txtName.setText(userName + "님 서버 목록");

        //뷰 초기화
        listServer = (ListView)root.findViewById(R.id.List_server);

        //서버리스트뷰
        serverListAdapter = new ServerListAdapter();

        //태스트용으로 집어넣음
        serverListAdapter.addItem("TestServer", "Admin: K1A2", "People: 123456789000000");
        serverListAdapter.addItem("TestServer2222", "Admin: ...?", "People: 0");

        listServer.setAdapter(serverListAdapter);//

        //리스트뷰 롱클릭 처리
        listServer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                serverListItem = (ServerListItem) serverListAdapter.getItem(pos);
                AlertDialog.Builder alert = new AlertDialog.Builder(root.getContext());
                alert.setTitle(String.format("\'%s\'를 목록에서 지우시겠습니까?", serverListItem.getSvName()));
                alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        serverListAdapter.remove(pos);
                        Toast.makeText(root.getContext(), String.format("\'%s\'를 목록에서 지웠습니다", serverListItem.getSvName()), Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("취소", null);
                alert.show();
                return true;
            }
        });//
    }
}
