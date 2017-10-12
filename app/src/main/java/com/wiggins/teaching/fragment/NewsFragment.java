package com.wiggins.teaching.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.wiggins.teaching.R;
import com.wiggins.teaching.activity.SearchActivity;
import com.wiggins.teaching.appconstant.AppConstant;
import com.wiggins.teaching.entity.TabEntity;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.network.result.CatResult;
import com.wiggins.teaching.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wiggins.teaching.activity.MainActivity.createHeader;
import static com.wiggins.teaching.appconstant.AppConstant.TOAST_TYPE_FALSE;


/**
 * 作者：wiggins on 2017/4/13 09:36
 * 邮箱：traywangjun@gmail.com
 */

public class NewsFragment extends BaseFragment {

    @BindView(R.id.ctl_microblog)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.vp_dynamic)
    ViewPager mViewPager;
    private ArrayList<NewsListFragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();


    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void init() {
        super.init();

        setTvTitleBackgroundColor(AppConstant.STATUS_BAR_COLOR);

        getCate();

        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCommonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void backKeyEvent() {

    }

    /**
     * 获取分类
     */
    private void getCate() {
        Map<String, String> map = HttpRequestParam.createGetMap(false);
        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_CAT, createHeader(),
                HttpTypeConstant.HTTP_TYPE_CAT, getRequestTag(), map);
        RetrofitManager.getInstance().request(body, new OnRequestCallBack<CatResult>() {
            @Override
            public void onSuccess(CatResult tClass, String type) {
                for (int i = 0; i < tClass.getData().size(); i++) {
                    mTabEntities.add(new TabEntity(tClass.getData().get(i).getCatname(), 0, 0));
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", i);
                    bundle.putInt("catid", tClass.getData().get(i).getCatid());
                    NewsListFragment listFragment = NewsListFragment.newInstance();
                    listFragment.setArguments(bundle);
                    mFragments.add(listFragment);
                }
                mCommonTabLayout.setTabData(mTabEntities);
                mViewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
                mViewPager.setOffscreenPageLimit(tClass.getData().size());
                mCommonTabLayout.setCurrentTab(0);
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }


    @OnClick(R.id.ibtn_search)
    void search() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
