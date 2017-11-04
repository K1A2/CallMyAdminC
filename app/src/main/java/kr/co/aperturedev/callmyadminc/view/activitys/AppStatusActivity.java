package kr.co.aperturedev.callmyadminc.view.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.aperturedev.callmyadminc.R;

/**
 * Created by jckim on 2017-11-05.
 */

public class AppStatusActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_status, container, false);

        return root;
    }
}
