package com.wiggins.teaching.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.widget.Toast;

import com.lib.base.util.AppUtils;
import com.lib.base.util.Utils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.wiggins.teaching.appconstant.AppConstant;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * <pre>
 *     author ：wiggins on 2017/4/14 15:56
 *     e-mail ：traywangjun@gmail.com
 *     desc   : Application
 *     version: 1.0
 * </pre>
 */
public class App extends Application {

    private static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        this.init();
    }

    /**
     * 初始化一些数据,但是不要做太多的事情
     */
    private void init() {
        mApp = this;
        Utils.init(this);

        if (AppUtils.isAppDebug(this)) {
            debug();
        } else {
            release();
        }

    }

    private void showToast(final String text){

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public static App getAppContext() {
        return mApp;
    }

    /**
     * 发布版本的时候需要把日志和一些debug工具的状态改变
     */
    private void release() {
        ButterKnife.setDebug(false);
        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        Logger.init(AppConstant.LOG_TAG).logLevel(LogLevel.NONE);
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
    }

    /**
     * debug 状态
     */
    private void debug() {
        openStrictMode();
        ButterKnife.setDebug(true);
        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        Logger.init(AppConstant.LOG_TAG).logLevel(LogLevel.FULL);
    }


    /**
     * 打开严格模式
     */
    private void openStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }
}
