package com.wiggins.teaching.network.constant;

/**
 * Created by jarylan on 2017/3/21.
 * 所有接口地址；
 * 若地址变动，统一在此处修改
 */
public class HttpUrlConstant {

    /* Base Url */
    //TODO 请填写外网能访问的地址
    public final static String BASE_URL = "http://192.168.2.237:8888/";

    public final static String URL_INIT = "api/v1/init";//初始化接口
    public final static String URL_INDEX = "api/v1/index";//首页
    public final static String URL_CAT = "api/v1/cat";//分类
    public final static String URL_NEWS = "api/v1/news";//新闻
    public final static String URL_NEWS_DETAIL = "api/v1/news/";//新闻详情
    public final static String URL_NEWS_NEWSDETAIL_UPVOTE = " api/v1/upvote/";//新闻点赞详情
    public final static String URL_NEWS_UPVOTE = "api/v1/upvote";//新闻点赞详情
    public final static String URL_NEWS_DETAIL_RECOMMENT = "api/v1/rank";//新闻详情推荐
    public final static String URL_LOGIN = "api/v1/login";//登录
    public final static String URL_IDENTIFY = "api/v1/identify";//验证码
    public final static String URL_USER_INFO = "api/v1/user/1";//用户信息
    public final static String URL_UPLOAD_AVATAR = "api/v1/image";//上传图片接口
    public final static String URL_COMMENT = "api/v1/comment";//评论
    public final static String URL_COMMENT_LIST = "api/v1/comment/";//评论列表
}
