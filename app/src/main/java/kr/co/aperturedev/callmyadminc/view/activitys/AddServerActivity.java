package kr.co.aperturedev.callmyadminc.view.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import kr.co.aperturedev.callmyadminc.R;
import kr.co.aperturedev.callmyadminc.internet.http.HttpRequester;
import kr.co.aperturedev.callmyadminc.internet.http.OnHttpRequestListener;
import kr.co.aperturedev.callmyadminc.internet.http.RequestURLS;
import kr.co.aperturedev.callmyadminc.view.custom.ProgressManager;

/**
 * Created by jckim on 2017-10-29.
 */

public class AddServerActivity extends Fragment {
    private EditText searchBar = null;          // 검색 창
    private TextView serverListPrep = null;     // 검색 준비
    private ListView serverList = null;         // 검색 결과

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_addsv, container, false);
        this.searchBar = (EditText) root.findViewById(R.id.addsv_search_editor);
        this.searchBar.setOnEditorActionListener(new OnSearchBarActionListener());
        this.serverList = (ListView) root.findViewById(R.id.addsv_server_list);
        this.serverListPrep = (TextView) root.findViewById(R.id.addsv_server_list_prep);

        return root;
    }

    private void searchServers(String q) {
        if(q.length() == 0) return;
        // q 에 대한 검색을 실시한다.

        try {
            ProgressManager pm = new ProgressManager(getActivity());
            pm.setCancelable(false);
            pm.setMessage("검색 중 입니다...");
            pm.enable();

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("search-word", q);
            HttpRequester requester = new HttpRequester(jsonObj, RequestURLS.SERVER_SEARCH);
            requester.setListener(new OnSearchListener(pm));
            requester.start();

        } catch(Exception ex) {
            Toast.makeText(getActivity(), "Request failed", Toast.LENGTH_SHORT).show();
            return;
        }
    }



    class OnSearchListener implements OnHttpRequestListener {
        private ProgressManager pm = null;

        public OnSearchListener(ProgressManager pm) {
            this.pm = pm;
        }

        @Override
        public void onResponse(boolean isSucc, JSONObject jsonObj) {
            this.pm.disable();

            if(isSucc) {
                serverList.setVisibility(View.VISIBLE);
                serverListPrep.setVisibility(View.INVISIBLE);
            } else {
                serverList.setVisibility(View.INVISIBLE);
                serverListPrep.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "서버 검색에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.e("cma", jsonObj.toString());
        }
    }

    class OnSearchBarActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if(i != EditorInfo.IME_ACTION_SEARCH) return false;

            String q = textView.getText().toString();
            if(q.length() == 0) {
                Toast.makeText(getActivity(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                return false;
            }

            searchServers(q);
            return true;
        }
    }
}
