package com.wiggins.teaching.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.lib.base.util.HandlerUtils;
import com.lib.base.util.RegexUtils;
import com.wiggins.teaching.R;
import com.wiggins.teaching.appconstant.AppConstant;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.network.result.LoginResult;
import com.wiggins.teaching.ui.base.BaseFragment;
import com.wiggins.teaching.utils.SettingPrefences;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;

import static com.wiggins.teaching.activity.EditInformationActivity.getEditTextString;
import static com.wiggins.teaching.activity.MainActivity.createHeader;
import static com.wiggins.teaching.appconstant.AppConstant.TOAST_TYPE_FALSE;


/**
 * 作者：wiggins on 2017/4/13 09:36
 * 邮箱：traywangjun@gmail.com
 */

public class LoginFragment extends BaseFragment implements HandlerUtils.OnReceiveMessageListener {

    private int type = 1;
    @BindView(R.id.login_type)
    TextView loginType;
    @BindView(R.id.btn_login_type)
    Button btnLoginType;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_psw)
    LinearLayout llPsw;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_free_phone)
    EditText etFreePhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_password)
    EditText etPassword;

    private HandlerUtils.HandlerHolder mHandlerHolder;
    private static final int REFRESH_FRAGMENT_STATUS = 1;

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init() {
        super.init();
        setTvTitleBackgroundColor(AppConstant.STATUS_BAR_COLOR);
        mHandlerHolder = new HandlerUtils.HandlerHolder(this);
    }

    @Override
    public void backKeyEvent() {

    }

    @OnClick(R.id.tv_send_code)
    void SendCode() {
        if (RegexUtils.isMobileSimple(etFreePhone.getText().toString())) {
            getCode();
        } else {
            toastShort("请输入正确的手机号码", TOAST_TYPE_FALSE);
        }

    }

    /**
     * 获取验证码
     */
    private void getCode() {
        showLoadingDialog("正在发送...");
        Map<String, RequestBody> map = HttpRequestParam.createCodeParam(etFreePhone.getText().toString());

        HttpParamBody<RequestBody> body = new HttpParamBody<>(HttpUrlConstant.URL_IDENTIFY,
                createHeader(), HttpTypeConstant.HTTP_TYPE_IDENTIFY, getRequestTag(), map);

        RetrofitManager.getInstance().requestPost(body, new OnRequestCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject tClass, String type) {
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                dismissLoadingDialog();
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }


    /**
     * 登录
     */
    private void login() {
        showLoadingDialog("正在登录...");
        Map<String, String> map;
        if (type == 1) {
            map = HttpRequestParam.createLoginParam(getEditTextString(etPhone), getEditTextString(etPassword), type);
        } else {
            map = HttpRequestParam.createLoginParam(getEditTextString(etFreePhone), getEditTextString(etCode), type);
        }

        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_LOGIN,
                createHeader(), HttpTypeConstant.HTTP_TYPE_LOGIN, getRequestTag(), map);

        RetrofitManager.getInstance().request(body, new OnRequestCallBack<LoginResult>() {

            @Override
            public void onSuccess(LoginResult tClass, String type) {
                dismissLoadingDialog();
                SettingPrefences.getIntance().setIsLoginValue(true);
                SettingPrefences.getIntance().setTokenValue(tClass.getData().getToken());
                mHandlerHolder.sendEmptyMessage(REFRESH_FRAGMENT_STATUS);
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                dismissLoadingDialog();
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }

    @OnClick(R.id.btn_login)
    void loginIn() {
        login();
    }

    @OnClick(R.id.btn_login_type)
    void loginType() {
        if (type == 1) {
            loginType.setText("免密码登录");
            btnLoginType.setText("账号密码登录");
            llPhone.setVisibility(View.VISIBLE);
            etCode.setVisibility(View.VISIBLE);
            tv_hint.setVisibility(View.VISIBLE);
            etPhone.setVisibility(View.GONE);
            llPsw.setVisibility(View.GONE);
            type = 2;
        } else {
            loginType.setText("账号密码登录");
            btnLoginType.setText("免密码登录");
            etPhone.setVisibility(View.VISIBLE);
            llPsw.setVisibility(View.VISIBLE);
            tv_hint.setVisibility(View.INVISIBLE);
            llPhone.setVisibility(View.GONE);
            etCode.setVisibility(View.GONE);
            type = 1;
        }
    }

    @Override
    public void handlerMessage(Message msg) {
        switch (msg.what) {
            case REFRESH_FRAGMENT_STATUS:
                MineFragment newFragment = new MineFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.login_fragment_container, newFragment);
                transaction.commit();
                break;
            default:
                break;
        }
    }
}
