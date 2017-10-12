package com.wiggins.teaching.network;

import android.widget.Toast;

import com.google.gson.JsonObject;
import com.lib.base.net.BaseRetrofitManager;
import com.lib.base.net.HttpParamBody;
import com.lib.base.net.callback.OnRequestCallBack;
import com.lib.base.net.resultbean.BaseResult;
import com.lib.base.util.NetworkUtils;
import com.orhanobut.logger.Logger;
import com.wiggins.teaching.R;
import com.wiggins.teaching.app.App;
import com.wiggins.teaching.network.constant.HttpTypeConstant;
import com.wiggins.teaching.network.constant.HttpUrlConstant;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jarylan on 2017/3/21.
 * 对外网络接口的管理类；
 */
public class RetrofitManager extends BaseRetrofitManager {

    private static RetrofitApiService mRetrofitApiService;
    private static RetrofitManager mRetrofitManager;

    private RetrofitManager() {

    }

    /**
     * 网络管理类单例
     *
     * @return 单例
     */
    public static RetrofitManager getInstance() {
        if (mRetrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = new RetrofitManager();
                    REQUEST_ERROR_MSG = App.getAppContext().getString(R.string.tip_request_error);
                    NETWORK_UNAVAILABLE_MSG = App.getAppContext().getString(R.string.tip_network_unavailable);
                    SERVER_RESULT_ERROR_MSG = App.getAppContext().getString(R.string.tip_server_result_error);
                    NETWORK_CONNECT_TIMEOUT = App.getAppContext().getString(R.string.tip_network_timeout);
                    initRetrofit();
                }
            }
        }
        return mRetrofitManager;
    }

    private static void initRetrofit() {
        File cacheDir = new File(App.getAppContext().getCacheDir(), "response");
        //缓存的最大尺寸10m
        Cache cache = new Cache(cacheDir, 1024 * 1024 * 10);
        CacheInterceptor mCacheInterceptor = new CacheInterceptor();
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(mCacheInterceptor)
                .build();
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(HttpUrlConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofitApiService = mRetrofit.create(RetrofitApiService.class);
    }

    /**
     * 通用 POST 请求
     * 直接返回 JsonObject
     */
    public void requestPost(HttpParamBody<RequestBody> paramBody, OnRequestCallBack<JsonObject> onRequestCallBack) {
        Logger.d("-------- net base HttpParamBody : " + paramBody);
        if (!NetworkUtils.isConnected()) {
            networkAvailable(onRequestCallBack, paramBody.getHttpType());
            return;
        }
        Call<JsonObject> call = mRetrofitApiService.requestPost(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
        enqueue(call, paramBody, onRequestCallBack);
    }

    /**
     * 通用 GET 请求
     */
    public void requestGet(HttpParamBody<String> paramBody, OnRequestCallBack<JsonObject> onRequestCallBack) {
        Logger.d("-------- net base HttpParamBody : " + paramBody);
        Call<JsonObject> call = mRetrofitApiService.requestGet(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
        enqueue(call, paramBody, onRequestCallBack);
    }

    /**
     * 通用 Put 请求
     */
    public void requestPut(HttpParamBody<String> paramBody, OnRequestCallBack<JsonObject> onRequestCallBack) {
        Logger.d("-------- net base HttpParamBody : " + paramBody);
        Call<JsonObject> call = mRetrofitApiService.getEditInfoApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
        enqueue(call, paramBody, onRequestCallBack);
    }

    /**
     * 通用 Delete 请求
     */
    public void requestDelete(HttpParamBody<String> paramBody, OnRequestCallBack<JsonObject> onRequestCallBack) {
        Logger.d("-------- net base HttpParamBody : " + paramBody);
        Call<JsonObject> call = mRetrofitApiService.getCancelPraiseApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
        enqueue(call, paramBody, onRequestCallBack);
    }

    private void enqueue(Call<JsonObject> call, final HttpParamBody paramBody, final OnRequestCallBack<JsonObject> onRequestCallBack) {
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                callBackJson(true, onRequestCallBack, paramBody.getHttpType(), response, call.isCanceled());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Logger.d("--------------net base onFailure : " + t.getMessage());
                if (NetworkUtils.isConnected() && !call.isCanceled()) {//有网络还回调失败
                    if (checkFailureMsg(t, onRequestCallBack, paramBody)) return;
                }
                callBackJson(false, onRequestCallBack, paramBody.getHttpType(), null, call.isCanceled());
            }
        });
        callMap.put(paramBody.getTag(), call);
    }

    /**
     * 对外网络请求接口，View 层调用
     *
     * @param paramBody         封装的请求参数
     * @param onRequestCallBack 网络回调
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseResult> void request(final HttpParamBody paramBody, final OnRequestCallBack<T> onRequestCallBack) {
        Logger.d("-------- net base HttpParamBody : " + paramBody);
        /* 如果是 Post 请求 ，需要判断有无网络； Get 有缓存，故不需要判断 */
        if (paramBody.getMap().get("request.type") == null && !NetworkUtils.isConnected()) {
            networkAvailable(onRequestCallBack, paramBody.getHttpType());
            return;
        }
        Call<T> call = null;
        switch (paramBody.getHttpType()) {
            case HttpTypeConstant.HTTP_TYPE_INIT://初始化
                call = mRetrofitApiService.getInitApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_INDEX://首页
                call = mRetrofitApiService.getIndexApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_CAT://分类
                call = mRetrofitApiService.getCatApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_NEWS://新闻
                call = mRetrofitApiService.getNewsApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_NEWSDETAIL://新闻详情
                call = mRetrofitApiService.getNewsDetailApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_NEWSDETAIL_UPVOTE://新闻详情是否点赞
                call = mRetrofitApiService.getNewsDetailPriaseApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_UPVOTE://新闻详情点赞
                call = mRetrofitApiService.getPraiseApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_NEWSDETAIL_RECOMMENT://新闻详情推荐
                call = mRetrofitApiService.getNewsDetailRecommentApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_LOGIN://登录
                call = mRetrofitApiService.getLoginApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_IDENTIFY://发送验证码
                call = mRetrofitApiService.requestPost(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_USER_INFO://用户信息
                call = mRetrofitApiService.getUserInfoApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_EDIT_USER_INFO://修改用户信息
                call = mRetrofitApiService.getEditInfoApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_COMMENT://评论
                call = mRetrofitApiService.getCommentApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;
            case HttpTypeConstant.HTTP_TYPE_COMMENT_LIST://评论列表
                call = mRetrofitApiService.getCommentListApi(paramBody.getUrl(), paramBody.getHeader(), paramBody.getMap());
                break;

            default:

                //TODO 你没有传 HttpTypeConstant 常量, 打包时删掉该句
                Toast.makeText(App.getAppContext(), "你没有传 HttpTypeConstant 常量", Toast.LENGTH_SHORT).show();

                break;
        }
        if (call != null) {
            callMap.put(paramBody.getTag(), call);
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    callMap.remove(paramBody.getTag());
                    callBackBean(true, onRequestCallBack, paramBody, response, call.isCanceled(), null);
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    callMap.remove(paramBody.getTag());
                    if (NetworkUtils.isConnected() && !call.isCanceled()) {//有网络还回调失败
                        if (checkFailureMsg(t, onRequestCallBack, paramBody)) return;
                    }
                    callBackBean(false, onRequestCallBack, paramBody, null, call.isCanceled(), t.getMessage());
                }
            });
        }
    }

    /**
     * 检测 错误类型 ； 服务器数据问题或者超时
     */
    private <T> boolean checkFailureMsg(Throwable t, OnRequestCallBack<T> onRequestCallBack, HttpParamBody paramBody) {
        Logger.d("--------------net base onFailure : " + t.getMessage());
        if (t.getMessage() != null) {
            if (t.getMessage().contains("IllegalStateException") || t.getMessage().contains("JSON")) {
                //服务器返回格式有误的提示 java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BOOLEAN at line 1 column 32 path $.data
                //或者 Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $
                //TODO 返回 Json 格式与协定好的不一致， 统计  接口类型
                serverResultError(onRequestCallBack, paramBody.getHttpType());
                return true;
            } else if (t.getMessage().contains("after 10000ms") || t.getMessage().contains("timeout")) {
                //网络链接超时 failed to connect to c.youyoudon.com/118.178.122.152 (port 80) after 10000ms
                connectTimeOut(onRequestCallBack, paramBody.getHttpType());
                return true;
            }
        }
        return false;
    }
}
