package com.lib.base.net.callback;

import com.lib.base.net.BaseRetrofitManager;

/**
 * Created by jarylan on 2017/3/21.
 * 网络请求统一回调，即使后续更换网络框架，此类不变，替换框架不牵扯 View 层 ；
 */
public interface OnRequestCallBack<T> {

    //TODO 暂时没用上，后续考虑是否重新定义，且有点乱
    int FAILURE_TYPE_SERVER_RESOLVE = 1;
    int FAILURE_TYPE_NETWORK_RESOLVE = 2;
    int FAILURE_TYPE_LOGIC_TIPS = 3;

    /**
     * 访问网络数据成功回调
     * @param tClass 泛型，返回 JsonObject 或者已解析好的实体类
     * @param type 请求接口类型
     */
    void onSuccess(T tClass, String type);

    /**
     * 访问网络失败 或者 数据未获取下来
     * @param errorMsg 错误信息； 目前错误信息内容分三类：
     *                                      1. 无网络，提示语{@link BaseRetrofitManager#NETWORK_UNAVAILABLE_MSG 中，读取 xml string 值，可自定义}
     *                                      2. 请求过程中异常 ， 提示语在{@link BaseRetrofitManager#REQUEST_ERROR_MSG 中，读取 xml string 值，可自定义}
     *                                      3. 服务器返回，返回 code 不同，服务器会直接返回可交互提示语
     * @param failureType 错误类型：
     *                          1. 请求直接失败类型
     *                          2. 无网络 或者 请求有结果返回，但获取解析数据异常类型
     *                          3. 逻辑提示（如已注册），此类型可直接弹出提示, 泛指服务器返回提示语
     * @param type 请求接口类型
     * */
    void onFailure(String errorMsg, int failureType, String type);

}
