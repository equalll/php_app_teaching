package com.wiggins.teaching.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.wiggins.teaching.app.App;


/**
 * <pre>
 *     author : jarylan
 *     e-mail : jarylan@foxmail.com
 *     time   : 2017/05/17
 *     desc   : 用户相关一些轻量级配置信息，保存本地
 *     version: 1.0
 * </pre>
 */
public class SettingPrefences {

    private static final String SHAREDP_NAME = "wzly_user_configure";
    private static final String KEY_USER_TOKEN = "key.user.token";//Token key
    private static final String KEY_USER_ID = "key.user.id";//id key
    private static final String KEY_USER_ISLOGIN = "key.user.islogin";//islogin key
    private static SharedPreferences mSharedPreferences;
    private static SettingPrefences mPreferenceUtil;

    private SettingPrefences() {
        mSharedPreferences = App.getAppContext().getSharedPreferences(SHAREDP_NAME, Context.MODE_PRIVATE);
    }

    public static SettingPrefences getIntance() {
        if (null == mSharedPreferences) {
            mPreferenceUtil = new SettingPrefences();
        }
        return mPreferenceUtil;
    }

    /**
     * 清除所有轻量级配置信息
     */
    public void clearPrefences() {
        if (mSharedPreferences == null) {
            return;
        }
        mSharedPreferences.edit().clear().commit();
    }

    /**
     * 保存 Token 值
     *
     * @param token token 值
     */
    public void setTokenValue(String token) {
        mSharedPreferences.edit().putString(KEY_USER_TOKEN, token).apply();
    }

    /**
     * 获取保存的 Token 值
     *
     * @return Token 值
     */
    public String getTokenValue() {
        return mSharedPreferences.getString(KEY_USER_TOKEN, "");
    }

    /**
     * 保存 ID 值
     *
     * @param id id 值
     */
    public void setIDValue(String id) {
        mSharedPreferences.edit().putString(KEY_USER_ID, id).apply();
    }

    /**
     * 获取保存的 ID 值
     *
     * @return ID 值
     */
    public String getIDValue() {
        return mSharedPreferences.getString(KEY_USER_ID, "");
    }

    /**
     * 保存 islogin 值
     *
     * @param islogin islogin 值
     */
    public void setIsLoginValue(Boolean islogin) {
        mSharedPreferences.edit().putBoolean(KEY_USER_ISLOGIN, islogin).apply();
    }

    /**
     * 获取保存的 islogin 值
     *
     * @return islogin 值
     */
    public boolean getIsLoginValue() {
        return mSharedPreferences.getBoolean(KEY_USER_ISLOGIN, false);
    }


    /**
     * 设置字符串
     */
    public void setStringValue(String key, String value){
        mSharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * 获取字符串
     */
    public String getStringValue(String key){
        return mSharedPreferences.getString(key, "");
    }
}
