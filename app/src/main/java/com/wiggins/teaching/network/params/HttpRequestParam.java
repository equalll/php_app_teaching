package com.wiggins.teaching.network.params;

import android.support.v4.util.ArrayMap;

import com.lib.base.net.BaseHttpRequestParam;
import com.wiggins.teaching.utils.SettingPrefences;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by jarylan on 2017/3/21.
 * 所有请求 param Map 集 ；View 层调用返回 map 用于
 * 若接口参数改动，统一在此处修改
 */
public class HttpRequestParam extends BaseHttpRequestParam {

    /**
     * Post 请求, 创建带 token 的 Map；
     * Post 请求 字符串需要封装
     *
     * @param needToken 是否需要 put Token
     * @return Map
     */
    public static Map<String, RequestBody> createPostMap(boolean needToken) {
        Map<String, RequestBody> map = new ArrayMap<>();
        if (needToken) {
            map.put("access_user_token", parseRequestBody(SettingPrefences.getIntance().getTokenValue()));//登录后会将 token 存入 sp 中。此处统一获取
        }
        return map;
    }

    /**
     * GET 请求, 创建带 token 的 Map；
     * GET 请求 字符串不需要封装
     *
     * @param needToken 是否需要 put Token
     * @return Map
     */
    public static Map<String, String> createGetMap(boolean needToken) {
        Map<String, String> map = new ArrayMap<>();
        if (needToken) {
            map.put("access_user_token", SettingPrefences.getIntance().getTokenValue());//登录后会将 token 存入 sp 中。此处统一获取
        }
        map.put("request.type", "Get");
        return map;
    }

    /**
     * 登录
     * <p>
     * Post
     */
    public static Map<String, String> createLoginParam(String phone, String password, int type) {
        Map<String, String> map = new ArrayMap<>();
        map.put("phone", phone);
        if (type == 1) {
            map.put("password", password);
        } else {
            map.put("code", password);
        }
        return map;
    }

    /**
     * 获取验证码
     * <p>
     * Post
     */
    public static Map<String, RequestBody> createCodeParam(String phone) {
        Map<String, RequestBody> map = createPostMap(false);
        map.put("id", parseRequestBody(phone));
        return map;
    }

    /**
     * 获取新闻列表
     * <p>
     * GET
     */
    public static Map<String, String> createNewsList(int page, String catid, String size) {
        Map<String, String> map = createGetMap(false);
        map.put("catid", catid);
        map.put("size", size);
        map.put("page", page + "");
        return map;
    }

    /**
     * 获取搜索列表
     * <p>
     * GET
     */
    public static Map<String, String> createNewsList(String title) {
        Map<String, String> map = createGetMap(false);
        map.put("title", title);
        return map;
    }

    /**
     * 上传头像
     * <p>
     * POST
     */
    public static Map<String, RequestBody> createUploadAvatar(File file) {
        Map<String, RequestBody> map = createPostMap(true);
        map.put(parseImageMapKey("file", file.getName()), parseImageRequestBody(file));
        return map;
    }

    /**
     * 修改信息
     * <p>
     * PUT
     */
    public static Map<String, String> createEditUserInfo(String image, String username, String sex, String signture) {
        Map<String, String> map = createGetMap(false);
        map.put("image", image);
        map.put("username", username);
        map.put("sex", sex);
        map.put("signature", signture);
        return map;
    }

    /**
     * 设置密码
     * <p>
     * Get
     */
    public static Map<String, String> createSetPsw(String password) {
        Map<String, String> map = createGetMap(false);
        map.put("password", password);
        return map;
    }


    /**
     * 点赞
     * <p>
     * Post
     */
    public static Map<String, RequestBody> createSetPraise(String id) {
        Map<String, RequestBody> map = createPostMap(false);
        map.put("id", parseRequestBody(id));
        return map;
    }


    /**
     * 取消点赞
     * <p>
     * Delete
     */
    public static Map<String, String> createCanclePraise(String id) {
        Map<String, String> map = createGetMap(false);
        map.put("id", id);
        return map;
    }

    /**
     * 评论
     * <p>
     * Post
     */
    public static Map<String, RequestBody> createComment(String news_id,String content ,String to_user_id,String parent_id) {
        Map<String, RequestBody> map = createPostMap(false);
        map.put("news_id", parseRequestBody(news_id));
        map.put("content", parseRequestBody(content));
        if (!to_user_id.equals("")){
            map.put("to_user_id", parseRequestBody(to_user_id));
        }
        if (!parent_id.equals("")){
            map.put("parent_id", parseRequestBody(parent_id));
        }
        return map;
    }
}
