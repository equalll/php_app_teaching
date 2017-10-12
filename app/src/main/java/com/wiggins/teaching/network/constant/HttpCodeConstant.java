package com.wiggins.teaching.network.constant;


import com.lib.base.net.BaseHttpCodeConstant;

/**
 * Created by jarylan on 2017/4/6.
 * HttpCodeConstant
 */

public class HttpCodeConstant extends BaseHttpCodeConstant {

    /**
     * -1	网络超时，或者系统原因导致的错误
     * -2	签名错误
     * -3	用户id为空
     * -4	用户未登录
     * -5	图片上传类型错误
     * -6	返回类似错误
     * -7	错误的请求类型，如get请求用了post
     * -15	参数为空
     * 27060  被冻结
     * 27065  被注销
     * */
    public final static int CODE_ERROR_TIMEOUT = -1;
    public final static int CODE_ERROR_SIGNATURE = -2;
    public final static int CODE_ERROR_USER_ID_NULL = -3;
    public final static int CODE_ERROR_USER_UNKNOWN = -4;
    public final static int CODE_ERROR_PHOTO_TYPE = -5;
    public final static int CODE_ERROR_RETURN_TYPE = -6;
    public final static int CODE_ERROR_REQUEST_TYPE = -7;
    public final static int CODE_ERROR_PARAM_NULL = -15;

}
