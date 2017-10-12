package com.lib.base.net;

import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lib.base.net.callback.OnRequestCallBack;
import com.lib.base.net.resultbean.BaseResult;
import com.lib.base.observer.ConcreteSubject;
import com.lib.base.util.NetworkUtils;
import com.orhanobut.logger.Logger;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jarylan on 2017/3/22.
 * 网络请求结果逻辑处理
 * 项目中拓展请继承此类
 */

public class BaseRetrofitManager{

    protected static String REQUEST_ERROR_MSG;//访问接口失败提示语, 获取 xml string "tip_request_error" 字段
    protected static String NETWORK_UNAVAILABLE_MSG;//网络不可用, 获取 xml string "tip_network_unavailable" 字段
    protected static String SERVER_RESULT_ERROR_MSG;//服务器返回字段格式跟商定好的不一致, 获取 xml string "tip_server_result_error" 字段
    protected static String NETWORK_CONNECT_TIMEOUT;//网络链接超时, 获取 xml string "tip_network_timeout" 字段
    protected Map<String, Call> callMap = new ArrayMap<>();//存请求对象；

    /**
     * 通用请求返回 JsonObject 逻辑处理
     */
    private void callbackOnResponseJson(Response<JsonObject> response, OnRequestCallBack<JsonObject> onRequestCallBack, String httpType) {
        if (response.isSuccessful()) {
            JsonObject json = response.body();
            Logger.json(json.toString());
            resultCode(json, onRequestCallBack, httpType);
        } else {
            Logger.d("--------------net base onResponse :  response.isSuccessful() is false ; code = " + response.code());
            //判断网络； 是否是网络不可用
            if(!NetworkUtils.isConnected()){
                networkAvailable(onRequestCallBack,httpType);
                return;
            }
            callbackFail(onRequestCallBack, REQUEST_ERROR_MSG, OnRequestCallBack.FAILURE_TYPE_NETWORK_RESOLVE, httpType);
        }
    }

    /**
     * 通用请求返回 code 处理逻辑
     */
    private void resultCode(JsonObject json, OnRequestCallBack<JsonObject> onRequestCallBack, String httpType) {
        Gson gson = new Gson();
        BaseResult bean = gson.fromJson(json, BaseResult.class);
        switch (bean.getStatus()) {
            case BaseHttpCodeConstant.CODE_SUCCESS:
                if( httpType != null ) {
                    if (httpType.equals(BaseHttpTypeConstant.HTTP_TYPE_LOGIN)){
                        ConcreteSubject.getInstance().loginChange(bean.getStatus());//登录成功， 通知观察者们
                    }
                }
                if(onRequestCallBack != null) {
                    onRequestCallBack.onSuccess(json, httpType);
                }
                break;
            case BaseHttpCodeConstant.CODE_ERROR_FROZEN://冻结
                callbackFail(onRequestCallBack, bean.getMessage(), BaseHttpCodeConstant.CODE_ERROR_FROZEN, httpType);
                break;
            case BaseHttpCodeConstant.CODE_ERROR_WRITE_OFF://注销
                callbackFail(onRequestCallBack, bean.getMessage(), BaseHttpCodeConstant.CODE_ERROR_WRITE_OFF, httpType);
                break;
            case BaseHttpCodeConstant.CODE_ERROR_NULL_TOKEN:

            case BaseHttpCodeConstant.CODE_ERROR_INVALID_TOKEN:
                ConcreteSubject.getInstance().loginChange(bean.getStatus());//Token 问题，通知观察者们登录状态发生改变
            default:
                callbackFail(onRequestCallBack, bean.getMessage(), OnRequestCallBack.FAILURE_TYPE_LOGIC_TIPS, httpType);
                break;
        }
    }

    /**
     * 请求实体结果返回后的逻辑处理 onResponse
     *
     * @param response          结果体
     * @param onRequestCallBack 回调
     * @param httpType          接口类型
     */
    private <T extends BaseResult> void callbackOnResponse(Response<T> response, OnRequestCallBack<T> onRequestCallBack, String httpType) {
        if (response.isSuccessful()) {
            T t = response.body();
            Logger.json(response.body().toString());
            resultCode(t, onRequestCallBack, httpType);
        } else {
            Logger.d("--------------net base onResponse :  response.isSuccessful() is false ; code = " + response.code());
            //判断网络； 是否是网络不可用
            if(!NetworkUtils.isConnected()){
                networkAvailable(onRequestCallBack,httpType);
                return;
            }
            callbackFail(onRequestCallBack, REQUEST_ERROR_MSG, OnRequestCallBack.FAILURE_TYPE_NETWORK_RESOLVE, httpType);
        }
    }

    /**
     * 封装返回实体结果 code 处理逻辑
     */
    private <T extends BaseResult> void resultCode(T t, OnRequestCallBack<T> onRequestCallBack, String httpType) {
        switch (t.getStatus()) {
            case BaseHttpCodeConstant.CODE_SUCCESS:
                if( httpType != null ) {
                    if (httpType.equals(BaseHttpTypeConstant.HTTP_TYPE_LOGIN)) {
                        ConcreteSubject.getInstance().loginChange(t.getStatus());//登录成功， 通知观察者们
                    }
                }
                if(onRequestCallBack != null) {
                    onRequestCallBack.onSuccess(t, httpType);
                }
                break;
            case BaseHttpCodeConstant.CODE_ERROR_FROZEN://冻结
                callbackFail(onRequestCallBack, t.getMessage(), BaseHttpCodeConstant.CODE_ERROR_FROZEN, httpType);
                break;
            case BaseHttpCodeConstant.CODE_ERROR_WRITE_OFF://注销
                callbackFail(onRequestCallBack, t.getMessage(), BaseHttpCodeConstant.CODE_ERROR_WRITE_OFF, httpType);
                break;
            case BaseHttpCodeConstant.CODE_ERROR_NULL_TOKEN:
            case BaseHttpCodeConstant.CODE_ERROR_INVALID_TOKEN:
                ConcreteSubject.getInstance().loginChange(t.getStatus());//Token 问题，通知观察者们登录状态发生改变
            default:
                callbackFail(onRequestCallBack, t.getMessage(), OnRequestCallBack.FAILURE_TYPE_LOGIC_TIPS, httpType);
                break;
        }
    }

    /**
     * 请求失败逻辑 onFailure
     *
     * @param onRequestCallBack 回调
     * @param httpType          接口类型
     */
    private void callbackOnFailure(OnRequestCallBack onRequestCallBack, String httpType) {
        callbackFail(onRequestCallBack, REQUEST_ERROR_MSG, OnRequestCallBack.FAILURE_TYPE_SERVER_RESOLVE, httpType);
    }

    /**
     * 各类型失败总回调接口，回调到 View 层
     *
     * @param onRequestCallBack 回调
     * @param errorMsg          错误信息
     * @param type              错误类型
     * @param httpType          接口类型
     * @see OnRequestCallBack
     */
    private void callbackFail(OnRequestCallBack onRequestCallBack, String errorMsg, int type, String httpType) {
        if(onRequestCallBack != null) {
            onRequestCallBack.onFailure(errorMsg, type, httpType);
        }
    }

//    /**
//     * 对外
//     *
//     * @return 当前网络状态
//     */
//    protected boolean isNetworkAvailable(Context context) {
//        if (context != null) {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//            if (mNetworkInfo != null) {
//                return mNetworkInfo.isAvailable();
//            }
//        }
//        return false;
//    }

    /**
     * 网络不可用回调, 对外
     *
     * @param onRequestCallBack 回调
     * @param httpType          接口类型
     */
    protected void networkAvailable(OnRequestCallBack onRequestCallBack, String httpType) {
        callbackFail(onRequestCallBack, NETWORK_UNAVAILABLE_MSG, OnRequestCallBack.FAILURE_TYPE_NETWORK_RESOLVE, httpType);
    }

    /**
     * 服务器返回数据与商定好的不一致, 对外
     *
     * @param onRequestCallBack 回调
     * @param httpType          接口类型
     */
    protected void serverResultError(OnRequestCallBack onRequestCallBack, String httpType) {
        callbackFail(onRequestCallBack, SERVER_RESULT_ERROR_MSG, OnRequestCallBack.FAILURE_TYPE_SERVER_RESOLVE, httpType);
    }

    /**
     * 网络链接超时, 对外
     *
     * @param onRequestCallBack 回调
     * @param httpType          接口类型
     */
    protected void connectTimeOut(OnRequestCallBack onRequestCallBack, String httpType) {
        callbackFail(onRequestCallBack, NETWORK_CONNECT_TIMEOUT, OnRequestCallBack.FAILURE_TYPE_NETWORK_RESOLVE, httpType);
    }

    /**
     * 通用请求返回 JsonObject 逻辑处理
     *
     * @param result            请求是否有结果返回
     * @param onRequestCallBack 回调
     * @param httpType          接口类型
     * @param response          结果体
     */
    protected void callBackJson(boolean result, OnRequestCallBack<JsonObject> onRequestCallBack, String httpType, Response<JsonObject> response, boolean isCanceled) {
        if (isCanceled) {
            return;
        }
        if (result) {
            callbackOnResponseJson(response, onRequestCallBack, httpType);
            return;
        }
        callbackOnFailure(onRequestCallBack, httpType);
    }

    /**
     * 请求实体结果返回后的逻辑处理
     *
     * @param result            请求是否有结果返回
     * @param response          结果体
     * @param onRequestCallBack 回调
     * @param paramBody         请求参数类集
     * @param isCanceled        是否取消了此次请求
     */
    protected <T extends BaseResult> void callBackBean(boolean result, OnRequestCallBack<T> onRequestCallBack, HttpParamBody paramBody, Response<T> response, boolean isCanceled, String throwableMsg) {
        if (isCanceled) {
            return;
        }
        if (result) {
            callbackOnResponse(response, onRequestCallBack, paramBody.getHttpType());
            return;
        }
        Logger.d("--------------net base onFailure : " + throwableMsg);
        callbackOnFailure(onRequestCallBack, paramBody.getHttpType());
    }

    /**
     * 取消请求
     *
     * @param tag 请求发起的tag
     */
    public void cancelRequest(String tag) {
        Call call = callMap.get(tag);
        Logger.d("------------net base cancel request ：" + tag +"  " + call);
        if (call == null) return;
        callMap.remove(tag);
        if (call.isCanceled()) return;
        call.cancel();
    }
}
