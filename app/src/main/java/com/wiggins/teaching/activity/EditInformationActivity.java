package com.wiggins.teaching.activity;

import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.wiggins.teaching.R;
import com.wiggins.teaching.appconstant.AppConstant;
import com.wiggins.teaching.network.RetrofitManager;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;
import com.wiggins.teaching.network.params.HttpRequestParam;
import com.wiggins.teaching.network.result.UserInfoResult;
import com.wiggins.teaching.ui.view.ActionSheetDialog;
import com.wiggins.teaching.ui.view.SelectableRoundedImageView;
import com.wiggins.teaching.ui.view.SingleRowPickerDialog;
import com.wiggins.teaching.utils.GlideUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;

import static com.wiggins.teaching.activity.MainActivity.createHeader;
import static com.wiggins.teaching.appconstant.AppConstant.TOAST_TYPE_FALSE;
import static com.wiggins.teaching.utils.AESHelper.decrypt;

/**
 * author ：wiggins on 2017/7/26 11:18
 * e-mail ：traywangjun@gmail.com
 * desc :
 * version :1.0
 */

public class EditInformationActivity extends PhotoSelectionBaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.iv_user_avatar_personal)
    SelectableRoundedImageView userAvatar;
    @BindView(R.id.et_edit_nick)
    EditText username;
    @BindView(R.id.et_edit_signture)
    EditText signature;
    String avatar, sex;

    @Override
    protected int getLayout() {
        return R.layout.acrivity_edit_information;
    }

    @Override
    protected boolean isOpenSideSlip() {
        return true;
    }

    @Override
    protected void init() {
        tvTitle.setText("编辑资料");
        getUserInfo();
    }

    @OnClick(R.id.ibtn_back)
    void close() {
        finish();
    }

    @OnClick(R.id.rela_personal_data)
    void setAvatar() {
        showHeadPortraitDialog();
    }

    @OnClick(R.id.btn_save)
    void save() {
        setUserInfo();
    }

    @OnClick(R.id.ll_edit_sex)
    void showSex() {
        Map<String, String> map = new HashMap<>();
        map.put("0", "保密");
        map.put("1", "男");
        map.put("2", "女");
        new SingleRowPickerDialog(this, "性别", map, sex, new SingleRowPickerDialog.OnPickerChangeListener() {
            @Override
            public void onPickerChange(String content, String contentId, boolean confirm) {
                if (confirm) {
                    tvSex.setText(content);
                    sex = contentId;
                }
            }
        }).show();
    }

    /**
     * 获取登录的用户基本信息
     */
    private void getUserInfo() {
        showLoadingDialog("正在加载...");
        Map<String, String> map = HttpRequestParam.createGetMap(false);

        HttpParamBody<String> body = new HttpParamBody<>(HttpUrlConstant.URL_USER_INFO,
                createHeader(), HttpTypeConstant.HTTP_TYPE_USER_INFO, getRequestTag(), map);

        RetrofitManager.getInstance().request(body, new OnRequestCallBack<UserInfoResult>() {
            @Override
            public void onSuccess(UserInfoResult tClass, String type) {
                dismissLoadingDialog();
                try {
                    JSONObject object = new JSONObject(decrypt(tClass.getData()));
                    avatar = object.optString("image");
                    sex = object.optString("sex");
                    GlideUtils.loadingAsRoundedImageView(getApplicationContext(), object.optString("image"), userAvatar);
                    username.setText(object.optString("username"));
                    signature.setText(object.optString("signature"));
                    tvSex.setText(object.optString("sex").equals("1") ? "男" : "女");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                dismissLoadingDialog();
                toastShort(errorMsg, TOAST_TYPE_FALSE);
            }
        });
    }


    /**
     * 修改用户基本信息
     */
    private void setUserInfo() {
        showLoadingDialog("正在加载...");
        Map<String, String> map = HttpRequestParam.createEditUserInfo(avatar, getEditTextString(username), sex, getEditTextString(signature));

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

    @Override
    protected void updatePic(File file) {
        if (null == file) {
            return;
        }
        showLoadingDialog(R.string.tip_upload);
        Map<String, RequestBody> map = HttpRequestParam.createUploadAvatar(file);
        Map<String, String> header = createHeader();
        header.put("contrent-type", "multipart/form-data");
        HttpParamBody<RequestBody> body = new HttpParamBody<>(HttpUrlConstant.URL_UPLOAD_AVATAR, header,
                HttpTypeConstant.HTTP_UPLOAD_AVATAR, getRequestTag(), map);
        RetrofitManager.getInstance().requestPost(body, new OnRequestCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject tClass, String type) {
                dismissLoadingDialog();
                avatar = tClass.getAsJsonObject().get("data").toString().replace("\"", "");
                GlideUtils.loadingAsRoundedImageView(getApplicationContext(), avatar, userAvatar);
            }

            @Override
            public void onFailure(String errorMsg, int failureType, String type) {
                dismissLoadingDialog();
                toastShort(errorMsg, AppConstant.TOAST_TYPE_FALSE);
            }
        });
    }


    /**
     * 显示修改头像Dialog
     */
    protected void showHeadPortraitDialog() {
        String[] strArray = getResources().getStringArray(R.array.hear_portrait);
        ActionSheetDialog dialog = new ActionSheetDialog(EditInformationActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true);

        dialog.addSheetItem(strArray[0], ActionSheetDialog.SheetItemColor.Blue,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        camera();
                    }
                });
        dialog.addSheetItem(strArray[1], ActionSheetDialog.SheetItemColor.Blue,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        openAlbum();
                    }
                });
        dialog.show();
    }


    public static String getEditTextString(EditText e) {
        return e.getEditableText().toString();
    }
}
