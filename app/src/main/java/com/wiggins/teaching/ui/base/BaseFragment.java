package com.wiggins.teaching.ui.base;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.base.util.BarUtils;
import com.lib.base.util.KeyboardUtils;
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
 *     desc   : BaseFragment
 *     version: 1.1
 * </pre>
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    protected View statusBar;
    protected Toast toast;
    protected TextView msg;
    protected ImageView img;
    protected View rootView;
    protected Dialog loadingDialog;

    protected abstract int getLayout();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, rootView);
        statusBar = rootView.findViewById(R.id.fake_status_bar);
        init();
        return rootView;
    }

    protected void init() {

    }

    protected void setTvTitleBackgroundColor(@ColorInt int color) {
        if (statusBar == null) {
            return;
        }
        statusBar.setBackgroundColor(color);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            params.height = BarUtils.getStatusBarHeight(getContext());
        } else {
            params.height = 0;
        }
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

    private void createToast() {
        if (toast == null) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.view_toast, null);
            msg = view.findViewById(R.id.msg);
            img = view.findViewById(R.id.img);

            toast = new Toast(getActivity());
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
        }
    }

    private void showToast(String s, int type) {
        msg.setText(s);
        if (type == AppConstant.TOAST_TYPE_FALSE) {
            img.setBackgroundResource(R.drawable.ic_attention);
        } else {
            img.setBackgroundResource(R.drawable.ic_tick);
        }
        toast.show();
    }

    public void toastShort(String s, int type) {
        createToast();
        toast.setDuration(Toast.LENGTH_SHORT);
        showToast(s, type);
    }

    public void showLoadingDialog(String tip) {
        loadingDialog = DialogUtils.showLoading(getContext(), tip);
        loadingDialog.show();
    }

    public void showLoadingDialog(int tip) {
        loadingDialog = DialogUtils.showLoading(getContext(), tip);
        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelRequest();
    }

    protected void cancelRequest() {
        RetrofitManager.getInstance().cancelRequest(getRequestTag());//取消网络请求
    }

    public void back() {
        getFragmentManager().popBackStack();
        boolean isKeyBoard = KeyboardUtils.keyBoardState(getContext(), rootView);
        if (isKeyBoard) {
            //如果键盘已经弹出，则隐藏键盘
            KeyboardUtils.hideKeyboard(getActivity());
        }
    }

    /**
     * fragment 监听返回按键事件
     * <p>
     * 使用方法：在fragment的onResume()方法中进行调用就可以实现事件的绑定
     */
    public void onBackPress() {
        //绑定返回按键事件
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    //调用返回按键事件
                    backKeyEvent();
                    return true;
                }
                return false;
            }
        });
    }


    /**
     * 自定义fragment返回事件
     */
    public abstract void backKeyEvent();

}
