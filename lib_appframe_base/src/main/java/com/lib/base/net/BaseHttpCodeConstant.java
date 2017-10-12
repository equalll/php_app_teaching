package com.lib.base.net;

/**
 * Created by jarylan on 2017/3/21.
 * 网络请求服务器返回 code 的常量集
 * 项目中拓展请继承此类
 */

public class BaseHttpCodeConstant {

    /**
     * 0	请求成功
     * -8	无效的token，则跳转到登录页面
     * -9	token不存在，跳转到登录页面
     * 27060  被冻结
     * 27065  被注销
     */
    public final static int CODE_SUCCESS = 1;
    public final static int CODE_ERROR_INVALID_TOKEN = -8;
    public final static int CODE_ERROR_NULL_TOKEN = -9;
    public final static int CODE_ERROR_FROZEN = 27060;
    public final static int CODE_ERROR_WRITE_OFF = 27065;

}
