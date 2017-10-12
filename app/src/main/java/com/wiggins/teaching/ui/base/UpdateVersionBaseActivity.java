package com.wiggins.teaching.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.lib.base.util.Utils;
import com.wiggins.teaching.R;
import com.wiggins.teaching.appconstant.AppConstant;
import com.wiggins.teaching.network.result.InitResult;
import com.wiggins.teaching.ui.view.AlertDialog;

/**
 * <pre>
 *     author : jarylan
 *     e-mail : jarylan@foxmail.com
 *     time   : 2017/06/15
 *     desc   : 版本检测更新 基类
 *     version: 1.0
 * </pre>
 */
public abstract class UpdateVersionBaseActivity extends BaseActivity {

    public static final String KEY_VERSION_CODE = "key.version.code";
    public static final String KEY_VERSION_NAME = "key.version.name";


    /**
     * @param isLoading 是否需要进度显示 ； 可区分在何处检测
     * @param force     true ; 只有在强制更新才会弹提示，false 都弹。 可区分是否在 闪屏页 检测
     */
    protected void checkVersion(InitResult tClass, boolean isLoading, boolean force) {
        boolean isShow = false;
        //比较版本
        InitResult.DataBean data = tClass.getData();
        if (data != null) {
            if (!isLoading && !force) {//判断是否上一次提示的版本，是则不弹 dialog
                String local_version_code = Utils.getSpUtils().getString(KEY_VERSION_CODE);
                String local_version_name = Utils.getSpUtils().getString(KEY_VERSION_NAME, "-1");
                if (local_version_code == data.getVersion() &&
                        local_version_name.equals(data.getVersion_code())) {
                    Utils.getSpUtils().put(KEY_VERSION_CODE, data.getVersion());
                    Utils.getSpUtils().put(KEY_VERSION_NAME, data.getVersion_code());
                    //如果没有弹出 更新 dialog ； 则回调弹 引导
                    onShowGuideDialog();
                    return;
                }
            }
            int currentCode = getVersionCode(UpdateVersionBaseActivity.this);
            if (!force) {
                //非闪屏页进来 ； 保存服务器推送版本 到本地
                Utils.getSpUtils().put(KEY_VERSION_CODE, data.getVersion());
                Utils.getSpUtils().put(KEY_VERSION_NAME, data.getVersion_code());
            }
            boolean isForce = data.getIs_force().equals("2");
            if (isForce) {
                //回调强制更新
                onForceUpdate();
            }
            if (Integer.valueOf(data.getVersion()) > currentCode) {
                //有新版本
                if (!TextUtils.isEmpty(data.getApk_url())) {
                    if (force) {//一定要强制更新才 弹
                        if (isForce) {
                            isShow = true;
                            versionUpdate(data.getVersion_code(),
                                    data.getUpgrade_point(),
                                    true,
                                    data.getApk_url());
                        }
                    } else {
                        isShow = true;
                        versionUpdate(data.getVersion_code(),
                                data.getUpgrade_point(),
                                isForce,
                                data.getApk_url());
                    }
                }
            } else {
                //已经是最新版本
                if (isLoading) {
                    toastShort(getString(R.string.tip_is_the_latest_version), AppConstant.TOAST_TYPE_TRUE);
                }
            }
        }
        if (!isShow) {
            //如果没有弹出 更新 dialog ； 则回调弹 引导
            onShowGuideDialog();
        }
    }

    /**
     * 获取版本号
     */
    private int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
    }


    /**
     * 版本更新提示
     *
     * @param isForce 是否强制更新
     */
    private void versionUpdate(String version, String content,
                               final boolean isForce, final String downloadUrl) {
        String dialogContent = getString(R.string.tip_new_version) + " : " + version;
        if (!TextUtils.isEmpty(content)) {
            dialogContent += "\n\n" + content;
        }
        AlertDialog updateDialog = new AlertDialog(this)
                .builder()
                .setUpdateMsg(dialogContent)
                .setCancelable(!isForce)
                .setPositiveButton(getString(R.string.dialog_update), !isForce,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Uri uri = Uri.parse(downloadUrl);
                                    Intent itnent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(itnent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
        if (!isForce) {
            updateDialog.setNegativeButton(getString(R.string.dialog_cancel),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
        }
        updateDialog.show();
        updateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //如果取消 dialog ； 则回调弹 引导
                onShowGuideDialog();
            }
        });
    }

    /**
     * 手动点击 更新 dilaog “取消”键；或者 检测失败
     */
    protected void onShowGuideDialog() {

    }

    /**
     * 强制更新回调
     */
    protected void onForceUpdate() {

    }
}
