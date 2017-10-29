package kr.co.aperturedev.callmyadminc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jckim on 2017-10-29.
 */

public class AddServer extends DialogFragment {

    private Context con;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_addsv, container, false);
        con = root.getContext();

        return root;
    }
}
