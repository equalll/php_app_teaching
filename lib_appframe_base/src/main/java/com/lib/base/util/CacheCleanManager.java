package com.lib.base.util;

import android.content.Context;
import android.os.Environment;

/**
 * <pre>
 *     author : jarylan
 *     e-mail : jarylan@foxmail.com
 *     time   : 2017/04/20
 *     desc   : 本应用数据清除管理器
 *     version: 1.0
 * </pre>
 */
public class CacheCleanManager {

    /**
     * 获取缓存大小 (包括内外部缓存)
     * 最小单位为 MB
     */
    public static String getCacheSize(Context context) {
        try {
            Long cacheSize;
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                cacheSize = FileSizeUtil.getFileSizes(context.getExternalCacheDir())
                        + FileSizeUtil.getFileSizes(context.getCacheDir());
            }else{
                cacheSize = FileSizeUtil.getFileSizes(context.getCacheDir()) ;
            }
            return FileSizeUtil.FormetFileSizeSpecifyMin(cacheSize,FileSizeUtil.SIZE_TYPE_MB);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.0MB";
    }

    /**
     * 清除 内外部缓存
     */
    public static void cleanCache(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
    }

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     */
    private static void cleanInternalCache(Context context) {
        FileUtils.deleteDir(context.getCacheDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    private static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            FileUtils.deleteDir(context.getExternalCacheDir());
        }
    }
}
