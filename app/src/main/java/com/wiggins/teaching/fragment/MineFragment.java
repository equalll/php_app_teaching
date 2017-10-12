package com.wiggins.teaching.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.wiggins.teaching.R;
import com.wiggins.teaching.activity.EditInformationActivity;
import com.wiggins.teaching.activity.SetPswActivity;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.network.result.UserInfoResult;
import com.wiggins.teaching.ui.base.BaseFragment;
import com.wiggins.teaching.ui.view.SelectableRoundedImageView;
import com.wiggins.teaching.utils.GlideUtils;
import com.wiggins.teaching.utils.SettingPrefences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.wiggins.teaching.activity.MainActivity.createHeader;
import static com.wiggins.teaching.appconstant.AppConstant.TOAST_TYPE_FALSE;
import static com.wiggins.teaching.utils.AESHelper.decrypt;

/**
 * author ：wiggins on 2017/7/25 11:11
 * e-mail ：traywangjun@gmail.com
 * desc :  我的
 * version :1.0
 */

public class MineFragment extends BaseFragment {


    @BindView(R.id.iv_user_avatar_personal)
    SelectableRoundedImageView userAvatar;
    @BindView(R.id.tv_username_personal)
    TextView username;
    @BindView(R.id.tv_signature_personal)
    TextView signature;
    boolean isFirstSetPSW;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }


    @Override
    protected void init() {
        getUserInfo();
    }

    /**
     * 获取登录的用户基本信息
     */
    private void getUserInfo() {
        Map<String, String> map = HttpRequestParam.createGetMap(false);

        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_USER_INFO,
                createHeader(), HttpTypeConstant.HTTP_TYPE_USER_INFO, getRequestTag(), map);

        RetrofitManager.getInstance().request(body, new OnRequestCallBack<UserInfoResult>() {
            @Override
            public void onSuccess(UserInfoResult tClass, String type) {
                try {
                    JSONObject object = new JSONObject(decrypt(tClass.getData()));
                    GlideUtils.loadingAsRoundedImageView(getActivity(), object.optString("image"), userAvatar);
                    username.setText(object.optString("username"));
                    signature.setText(object.optString("signature"));
                    if (object.optString("password").equals("")) {
                        isFirstSetPSW = true;
                    } else {
                        isFirstSetPSW = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }


    @OnClick(R.id.rela_personal_data)
    void editInfo() {
        startActivityForResult(new Intent(getActivity(), EditInformationActivity.class), 1);
    }

    @OnClick(R.id.rl_setpsw)
    void setPsw() {
        if (isFirstSetPSW) {
            startActivityForResult(new Intent(getActivity(), SetPswActivity.class), 1);
        } else {
            toastShort("您已设置过密码", TOAST_TYPE_FALSE);
        }

    }

    @OnClick(R.id.btn_out)
    void outLogin() {
        SettingPrefences.getIntance().clearPrefences();
        LoginFragment newFragment = new LoginFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mine_fragment_container, newFragment);
        transaction.commit();
    }

    @Override
    public void backKeyEvent() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            getUserInfo();
        }
    }
}
