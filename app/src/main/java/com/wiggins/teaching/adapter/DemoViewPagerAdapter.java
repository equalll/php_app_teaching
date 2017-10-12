package com.wiggins.teaching.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.wiggins.teaching.fragment.LoginFragment;
import com.wiggins.teaching.fragment.MineFragment;
import com.wiggins.teaching.fragment.NewsFragment;
import com.wiggins.teaching.fragment.RecommendFragment;
import com.wiggins.teaching.utils.SettingPrefences;

import java.util.ArrayList;

/**
 *
 */
public class DemoViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;

    public DemoViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.clear();
        fragments.add(NewsFragment.newInstance());
        fragments.add(RecommendFragment.newInstance());
        if (SettingPrefences.getIntance().getIsLoginValue()) {
            fragments.add(MineFragment.newInstance());
        } else {
            fragments.add(LoginFragment.newInstance());
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = (Fragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}