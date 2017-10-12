package com.wiggins.teaching.network;


import com.google.gson.JsonObject;
import com.wiggins.teaching.network.result.CatResult;
import com.wiggins.teaching.network.result.CommentListResult;
import com.wiggins.teaching.network.result.GetPraiseResult;
import com.wiggins.teaching.network.result.IndexListResult;
import com.wiggins.teaching.network.result.InitResult;
import com.wiggins.teaching.network.result.LoginResult;
import com.wiggins.teaching.network.result.NewsDetailResult;
import com.wiggins.teaching.network.result.NewsListResult;
import com.wiggins.teaching.network.result.RecommentNewsListResult;
import com.wiggins.teaching.network.result.UserInfoResult;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by jarylan on 2017/3/21.
 * 所有接口访问的定义；
 * 直接返回解析好的实体类，若想直接返回 JsonObject；则调用通用 POST 请求或者通用 GET 请求
 * 需要解析返回不同的实体，请追加方法
 * <p>
 * 注 ：POST 请求方法参数对应注解为 @PartMap
 * GET 请求方法参数对应注解为 @QueryMap
 */
public interface RetrofitApiService {

    @Multipart //POST 请求必须携带这个注解
    @POST
    // 通用 POST 请求 ； 上传图片或者文件直接调用封装好的 map 传进来即可 ， 参数注解为 @PartMap
    Call<JsonObject> requestPost(@Url String url, @HeaderMap Map<String, String> headers, @PartMap Map<String, RequestBody> map);

    @GET
    // 通用 GET 请求 ， 参数注解为 @QueryMap
    Call<JsonObject> requestGet(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> map);

    @Streaming//下载文件很大则使用 @Streaming 这个注解
    @GET
    //下载文件 ； 解析文件： body.contentLength()文件大小    body.byteStream(); 文件流
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @GET
    Call<InitResult> getInitApi(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> map);//初始化

    @GET
    Call<IndexListResult> getIndexApi(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> map);//首页

    @GET
    Call<CatResult> getCatApi(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> map);//分类

    @GET
    Call<NewsListResult> getNewsApi(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> map);//新闻

    @GET
    Call<NewsDetailResult> getNewsDetailApi(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> map);//新闻详情

    @GET
    Call<GetPraiseResult> getNewsDetailPriaseApi(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> map);//新闻详情

    @GET
    Call<RecommentNewsListResult> getNewsDetailRecommentApi(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> map);//新闻详情

//    @Multipart //POST 请求必须携带这个注解
//    @POST
//    Call<LoginResult> getLoginApi(@Url String url, @HeaderMap Map<String, String> headers, @PartMap Map<String, RequestBody> map);

    @FormUrlEncoded //POST 请求必须携带这个注解
    @POST
    Call<LoginResult> getLoginApi(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> map);

    @GET
    Call<UserInfoResult> getUserInfoApi(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> map);//用户信息

    @FormUrlEncoded
    @PUT
    Call<JsonObject> getEditInfoApi(@Url String url, @HeaderMap Map<String, String> header, @FieldMap Map<String, String> map);

    @Multipart //POST 请求必须携带这个注解
    @POST
    Call<JsonObject> getPraiseApi(@Url String url, @HeaderMap Map<String, String> headers, @PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @HTTP(method = "DELETE", hasBody = true)
    Call<JsonObject> getCancelPraiseApi(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> map);

    @Multipart //POST 请求必须携带这个注解
    @POST
    Call<JsonObject> getCommentApi(@Url String url, @HeaderMap Map<String, String> headers, @PartMap Map<String, RequestBody> map);

    @GET
    Call<CommentListResult> getCommentListApi(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> map);
}
