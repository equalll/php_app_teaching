package com.wiggins.teaching.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.wiggins.teaching.R;
import com.wiggins.teaching.ui.view.SelectableRoundedImageView;

/**
 * <pre>
 *     author : Jimmy.tsang
 *     e-mail : jimmytsangfly@gmail.com
 *     time   : 2017/03/30
 *     desc   : 图片加载工具类
 *     version: 1.0
 * </pre>
 * <p>
 * Glide特点
 * 1.使用简单
 * 2.可配置度高，自适应程度高
 * 3.支持常见图片格式 Jpg png gif webp
 * 4.支持多种数据源  网络、本地、资源、Assets 等
 * 5.高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
 * 6.生命周期集成   根据Activity/Fragment生命周期自动管理请求
 * 7.高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
 * 8.这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity</p>
 */
public class GlideUtils {
    // 參考文章链接 {@link http://blog.csdn.net/shangmingchao/article/details/51125554/}
    // Glide之GlideModule {@line http://blog.csdn.net/shangmingchao/article/details/51026742}


    /**
     * 默认加载图片
     *
     * @param context   context
     * @param path      图片地址
     * @param imageView 需要显示的ImageView
     */
    public static void loadImageViewLoading(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
        //TODO 还需要提供一个默认图片，直接在这里设置，方便以后修改
    }

    /**
     * 加载指定的大小图片
     *
     * @param context   context
     * @param path      图片地址
     * @param width     图片宽
     * @param height    图片高
     * @param imageView 需要显示的ImageView
     */
    public static void loadImageViewSize(Context context, String path, int width, int height, ImageView imageView) {
        Glide.with(context).load(path).override(width, height).into(imageView);
    }


    /**
     * 设置加载中以及加载失败图片并且指定大小
     *
     * @param context        context
     * @param path           图片地址
     * @param width          图片宽
     * @param height         图片高
     * @param imageView      需要显示的ImageView
     * @param loadingImage   加载中的图片
     * @param errorImageView 加载失败的图片
     */
    public static void loadImageViewLodingSize(Context context, String path, int width, int height, ImageView imageView, int loadingImage, int errorImageView) {
        Glide.with(context).load(path).override(width, height).placeholder(loadingImage).error(errorImageView).into(imageView);
    }

    /**
     * 设置跳过内存缓存
     *
     * @param context   context
     * @param path      图片地址
     * @param imageView 需要显示的ImageView
     */
    public static void loadImageViewCache(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).skipMemoryCache(true).into(imageView);
    }

    /**
     * 设置下载优先级
     *
     * @param context   context
     * @param path      图片地址
     * @param imageView 需要显示的ImageView
     */
    public static void loadImageViewPriority(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).priority(Priority.NORMAL).into(imageView);
    }

    /**
     * 针对 SelectableRoundedImageView{@link SelectableRoundedImageView} 的加载配置
     *
     * @param context context
     * @param path 图片地址
     * @param imageView 需要显示的ImageView
     */
    public static void loadingAsRoundedImageView(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .asBitmap()
                .placeholder(R.drawable.ic_default)
                .error(R.drawable.ic_load_failure)
                .into(imageView);
    }


}
