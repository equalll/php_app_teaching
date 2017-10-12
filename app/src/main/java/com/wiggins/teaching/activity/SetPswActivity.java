package com.wiggins.teaching.activity;

import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.wiggins.teaching.R;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.ui.base.BaseActivity;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wiggins.teaching.activity.EditInformationActivity.getEditTextString;
import static com.wiggins.teaching.activity.MainActivity.createHeader;
import static com.wiggins.teaching.appconstant.AppConstant.TOAST_TYPE_FALSE;

/**
 * author ：wiggins on 2017/7/27 14:44
 * e-mail ：traywangjun@gmail.com
 * desc :  设置密码
 * version :1.0
 */

public class SetPswActivity extends BaseActivity{


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.et_confirm_psw)
    EditText etConfirmPsw;

    @Override
    protected int getLayout() {
        return R.layout.activity_setpsw;
    }

    @Override
    protected boolean isOpenSideSlip() {
        return true;
    }

    @Override
    protected void init() {
        tvTitle.setText("设置密码");
    }

    @OnClick(R.id.btn_save)
    void save() {
        if (getEditTextString(etConfirmPsw).equals(getEditTextString(etPsw))){
            setUserInfo();
        }else{
            toastShort("请确认2次密码是否一致",TOAST_TYPE_FALSE);
        }
    }

    @OnClick(R.id.ibtn_back)
    void close() {
        finish();
    }

    /**
     * 修改用户基本信息
     */
    private void setUserInfo() {
        showLoadingDialog("正在加载...");
        Map<String, String> map = HttpRequestParam.createSetPsw(getEditTextString(etConfirmPsw));

        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_USER_INFO,
                createHeader(), HttpTypeConstant.HTTP_TYPE_EDIT_USER_INFO, getRequestTag(), map);

        RetrofitManager.getInstance().requestPut(body, new OnRequestCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject tClass, String type) {
                dismissLoadingDialog();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                dismissLoadingDialog();
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }
}
