package com.wiggins.teaching.network.constant;


import com.lib.base.net.BaseHttpTypeConstant;

/**
 * Created by jarylan on 2017/3/21.
 * 所有接口类型的枚举,由于枚举使用比常量使用的内存多 13 倍之多；所以改用常量
 * 主要用于标记哪个网络接口的回调
 */
public class HttpTypeConstant extends BaseHttpTypeConstant {

    public final static String HTTP_TYPE_INIT = "http.type.init"; // 初始化
    public final static String HTTP_TYPE_INDEX = "http.type.index"; // 首页
    public final static String HTTP_TYPE_CAT = "http.type.cat"; // 分类
    public final static String HTTP_TYPE_NEWS = "http.type.news"; // 新闻
    public final static String HTTP_TYPE_NEWSDETAIL = "http.type.newsdetail"; // 新闻详情
    public final static String HTTP_TYPE_NEWSDETAIL_RECOMMENT = "http.type.newsdetail.recomment"; // 新闻详情推荐
    public final static String HTTP_TYPE_NEWSDETAIL_UPVOTE = "http.type.newsdetail.upvote"; // 新闻详情是否点赞
    public final static String HTTP_TYPE_NEWSDETAIL_CANCLEUPVOTE = "http.type.newsdetail.cancleupvote"; // 新闻详情取消点赞
    public final static String HTTP_TYPE_UPVOTE = "http.type.upvote"; // 新闻详情点赞
    public final static String HTTP_TYPE_CANCLE_UPVOTE = "http.type.cancel.upvote"; // 新闻详情点赞
    public final static String HTTP_TYPE_IDENTIFY = "http.type.identify"; // 验证码
    public final static String HTTP_TYPE_USER_INFO = "http.type.userinfo"; // 用户信息
    public final static String HTTP_TYPE_EDIT_USER_INFO = "http.type.edit.userinfo"; // 修改用户信息
    public final static String HTTP_UPLOAD_AVATAR = "http.type.avatar"; // 上传图片接口
    public final static String HTTP_TYPE_COMMENT = "http.type.comment"; // 评论
    public final static String HTTP_TYPE_COMMENT_LIST = "http.type.comment.lsit"; // 评论列表

}
