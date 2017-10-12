package com.lib.base.net;

import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by jarylan on 2017/4/10.
 * 将网络请求所需参数封装
 */

public class HttpParamBody<T>{

    private String url;//拼接后半地址
    private String httpType;//自定义接口类型
    private String tag;//请求所在类 tag
    private Map<String,T> map;//请求参数
    private  Map<String, String> header;//头

    public HttpParamBody(String url, Map<String, String> header,@Nullable String httpType, String tag, Map<String, T> map) {
        this.url = url;
        this.httpType = httpType;
        this.tag = tag;
        this.map = map;
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpType() {
        return httpType;
    }

    public String getTag() {
        return tag;
    }

    public Map<String, T> getMap() {
        return map;
    }

    public Map<String, String> getHeader() {
        return header;
    }


    @Override
    public String toString() {
        return "HttpParamBody{" +
                "url='" + url + '\'' +
                ", httpType='" + httpType + '\'' +
                ", tag='" + tag + '\'' +
                ", map=" + map +
                ", header=" + header +
                '}';
    }
}
