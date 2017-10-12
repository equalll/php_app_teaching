package com.lib.base.net;

/**
 * Created by jarylan on 2017/3/21.
 * 所有接口类型的枚举,由于枚举使用比常量使用的内存多 13 倍之多；所以改用常量
 * 主要用于标记哪个网络接口的回调
 * 项目中拓展请继承此类
 */
public class BaseHttpTypeConstant {

    public final static String HTTP_TYPE_LOGIN = "http.type.login"; // 登录

}
