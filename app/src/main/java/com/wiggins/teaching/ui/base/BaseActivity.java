package com.wiggins.teaching.ui.base;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.base.util.BarUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.wiggins.teaching.R;
import com.wiggins.teaching.appconstant.AppConstant;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.utils.DialogUtils;

import butterknife.ButterKnife;


/**
 * <pre>
 *     author : Jimmy.tsang
 *     e-mail : jimmytsangfly@gmail.com
 *     time   : 2017/04/06
 *     desc   : 基类 Activity 尽量保持这个类简洁
 *     version: 1.1
 * </pre>
 */
public abstract class BaseActivity extends AppCompatActivity {

    private  long loginExceptionTime = 0;
    protected Toast toast;
    protected TextView msg;
    protected ImageView img;
    protected Dialog loadingDialog;

    protected abstract int getLayout();

    /**
     * 是否打开侧滑退出 Activity 功能；
     *
     * @return true 为打开 ， false 为关闭
     */
    protected abstract boolean isOpenSideSlip();

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeSetContentView();
        if (getLayout() != 0) {
            setContentView(getLayout());
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        initDefaultStatusBar();
        init();
        if (isOpenSideSlip()) OpenSideSlip();
    }

    /**
     * 默认设置状态栏 颜色
     * 重写该方法 来从新设置状态栏
     */
    protected void initDefaultStatusBar() {
        BarUtils.setColor(this, AppConstant.STATUS_BAR_COLOR);// 默认状态栏底色
    }

    protected void init() {

    }

    protected void initBeforeSetContentView() {

    }


    private void createToast() {
        if (toast == null) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.view_toast, null);
            msg = (TextView) view.findViewById(R.id.msg);
            img = (ImageView) view.findViewById(R.id.img);

            toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
        }
    }

    private Toast showToast(String s, int type) {
        msg.setText(s);
        if (type == AppConstant.TOAST_TYPE_FALSE) {
            img.setBackgroundResource(R.drawable.ic_attention);
        }else{
            img.setBackgroundResource(R.drawable.ic_tick);
        }
        toast.show();
        return toast;
    }

    public Toast toastShort(String s, int type) {
        createToast();
        toast.setDuration(Toast.LENGTH_SHORT);
        return showToast(s, type);
    }

    public void showLoadingDialog(String tip) {
        loadingDialog = DialogUtils.showLoading(this, tip);
        loadingDialog.show();
    }

    public void showLoadingDialog(int tip) {
        loadingDialog = DialogUtils.showLoading(this, tip);
        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    private void OpenSideSlip() {
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .build();
        Slidr.attach(this, config);
    }

    /**
     * 获取Tag标识，用于退出界面后取消请求
     *
     * @return 当前类名
     */
    protected String getRequestTag() {
        Class a = getClass();
        return a.getSimpleName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelRequest();
    }

    protected void cancelRequest(){
        RetrofitManager.getInstance().cancelRequest(getRequestTag());//取消网络请求
    }


}
