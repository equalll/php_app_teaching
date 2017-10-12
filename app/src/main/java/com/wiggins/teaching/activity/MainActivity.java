package com.wiggins.teaching.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.ArrayMap;
import android.view.KeyEvent;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.lib.base.util.AppUtils;
import com.lib.base.util.HandlerUtils;
import com.lib.base.util.TimeUtils;
import com.orhanobut.logger.Logger;
import com.wiggins.teaching.R;
import com.wiggins.teaching.adapter.DemoViewPagerAdapter;
import com.wiggins.teaching.app.App;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.network.result.InitResult;
import com.wiggins.teaching.ui.base.UpdateVersionBaseActivity;
import com.wiggins.teaching.utils.SettingPrefences;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.lib.base.util.PhoneUtils.getIMEI;
import static com.lib.base.util.PhoneUtils.getSystemModel;
import static com.lib.base.util.PhoneUtils.getSystemVersion;
import static com.wiggins.teaching.utils.AESHelper.encrypt;


/**
 * author ：wiggins on 2017/7/13 16:04
 * e-mail ：traywangjun@gmail.com
 * desc :  主页面
 * version :1.0
 */

public class MainActivity extends UpdateVersionBaseActivity implements
        HandlerUtils.OnReceiveMessageListener{

    private DemoViewPagerAdapter adapter;
    private AHBottomNavigationAdapter navigationAdapter;
    private int[] tabColors;

    // UI
    private AHBottomNavigationViewPager viewPager;
    private AHBottomNavigation bottomNavigation;

    public int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    
    @Override
    protected int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected boolean isOpenSideSlip() {
        return false;
    }

    @Override
    protected void init() {
        initUI();
    }

    /**
     * Init UI
     */
    private void initUI() {

        mHandlerHolder = new HandlerUtils.HandlerHolder(this);

        initJPush("1");

        getInfo();

        initApi();

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        viewPager = (AHBottomNavigationViewPager) findViewById(R.id.view_pager);

        tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
        navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);

        bottomNavigation.setAccentColor(ResourcesCompat.getColor(getResources(),R.color.dialog_blue,null));

        bottomNavigation.setTranslucentNavigationEnabled(true);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                viewPager.setCurrentItem(position, false);
                return true;
            }
        });

        viewPager.setOffscreenPageLimit(2);
        adapter = new DemoViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    private void getInfo() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * 创建头
     */
    public static Map<String, String> createHeader() {
        Map<String, String> map = new ArrayMap<>();
        map.put("sign", encrypt("did=" + getIMEI() + "&time=" + TimeUtils.getNowTimeMills()));
        map.put("version", AppUtils.getAppVersionName(App.getAppContext(), "com.wiggins.teaching"));
        map.put("app_type", "android");
        map.put("did", getIMEI());
        map.put("os", getSystemVersion());
        map.put("model", getSystemModel());
        if (SettingPrefences.getIntance().getIsLoginValue()) {
            map.put("access_user_token", SettingPrefences.getIntance().getTokenValue());
        }
        return map;
    }

    /**
     * 接口初始化
     */
    private void initApi() {
        showLoadingDialog("正在加载...");
//        Map<String, String> map = HttpRequestParam.createGetMap(false);
//        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_INIT, createHeader(),
//                HttpTypeConstant.HTTP_TYPE_INIT, getRequestTag(), map);
//        RetrofitManager.getInstance().request(body, new OnRequestCallBack<InitResult>() {
//            @Override
//            public void onSuccess(InitResult tClass, String type) {
//                checkVersion(tClass,false,false);
//                dismissLoadingDialog();
//            }
//
//            @Override
//            public void onFailure(String errorMsg, int failureType, String type) {
//                dismissLoadingDialog();
//            }
//        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            moveTaskToBack(true);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private HandlerUtils.HandlerHolder mHandlerHolder;
    private int SET_ALIAS = 1;
    Set<String> tags = new HashSet<>();

    /**
     * 极光
     */
    private void initJPush(String uid) {
        JPushInterface.init(this);
        mHandlerHolder.sendMessage(mHandlerHolder.obtainMessage(SET_ALIAS, uid));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            if (isFinishing()) {
                return;
            }
            String logs;
            Logger.e("Tag" + code + alias);
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Logger.e("Tag" + logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Logger.e("Tag" + logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    if (mHandlerHolder == null) break;
                    mHandlerHolder.sendMessageDelayed(
                            mHandlerHolder.obtainMessage(SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Logger.e("Tag" + logs);
            }
        }
    };

    @Override
    public void handlerMessage(Message msg) {
        switch (msg.what) {
            case 1:
                if (!isFinishing()) {
                    JPushInterface.setAliasAndTags(App.getAppContext(),
                            (String) msg.obj, tags, mAliasCallback);
                }
                break;
        }
    }
}
