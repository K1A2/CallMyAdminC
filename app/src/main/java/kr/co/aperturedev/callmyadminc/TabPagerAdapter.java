package kr.co.aperturedev.callmyadminc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kr.co.aperturedev.callmyadminc.view.activitys.AddServerActivity;
import kr.co.aperturedev.callmyadminc.view.activitys.AppStatusActivity;
import kr.co.aperturedev.callmyadminc.view.activitys.MainActivity;

/**
 * Created by jckim on 2017-11-04.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                MainActivity mainActivity = new MainActivity();
                return mainActivity;
            case 1:
                AddServerActivity addServerActivity = new AddServerActivity();
                return addServerActivity;
            case 2:
                AppStatusActivity appStatusActivity = new AppStatusActivity();
                return appStatusActivity;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
