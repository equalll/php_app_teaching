package com.wiggins.teaching.appconstant;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * <pre>
 *     author : Jimmy.tsang
 *     e-mail : jimmytsangfly@gmail.com
 *     time   : 2017/04/06
 *     desc   : App 常量
 *     version: 1.0
 * </pre>
 */
public class AppConstant {
    public static final String LOG_TAG = "wzly";
    public static final int CAMERA_REQUEST_CODE = 1;//拍照请求码
    public static final int OPEN_ALBUM_REQUEST_CODE = 2;//打开相册
    public static final int TOAST_TYPE_TRUE = 1;//正确的TOAST
    public static final int TOAST_TYPE_FALSE = 2;//错误的TOAST
    public static final String AES_KEY = "sgg45747sf#$5^&s";//aeskey
    public static final int REFRESH_ANIMATION = BaseQuickAdapter.ALPHAIN;//列表动画


    public static final int STATUS_BAR_COLOR = Color.parseColor("#1f1f1f");//默认状态栏颜色值
    public static final int REMAINING_NUMBER_PRELOADING = 7;//列表样式： 默认剩余多少条时预加载 。 （在下拉刷新后根据服务器返回具体每页总条数进行设置。）

    /*第一页开始下标*/
    public final static int FIRST_PAGE = 1;

    /**
     * 图片压缩
     */
    public static final int IMG_QUALITY = 80;
}
