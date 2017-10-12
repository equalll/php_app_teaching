package com.lib.base.util;

import android.os.Build;

/**
 * ================================
 * 作   者:   zcb
 * 邮   箱:   13405995634@163.com
 * 创建时间:   2017/6/1 17:50
 * 版   本:   1.0
 * 描   述:
 * =================================
 */


public class OSUtil {

    /**
     *
     * eg:int currentVersion = android.os.Build.VERSION.SDK_INT;
     *
     * VERSION.SDK_INT--------->19
     * VERSION.SDK--------->19
     * VERSION.CODENAME--------->REL
     * VERSION.INCREMENTAL--------->5.8.27
     * VERSION.RELEASE--------->4.4.4
     * VERSION_CODES.JELLY_BEAN--------->16
     * BOARD--------->MSM8974
     * BOOTLOADER--------->unknown
     * BRAND--------->Xiaomi
     * CPU_ABI--------->armeabi-v7a
     * CPU_ABI2--------->armeabi
     * DEVICE--------->cancro
     * DISPLAY--------->KTU84P
     * FINGERPRINT--------->Xiaomi/cancro_wc_lte/cancro:4.4.4/KTU84P/5.8.27:user/release-keys
     * HARDWARE--------->qcom
     * HOST--------->qh-miui-ota-bd58
     * ID--------->KTU84P
     * MANUFACTURER--------->Xiaomi
     * MODEL--------->MI 4LTE
     * PRODUCT--------->cancro_wc_lte
     * RADIO--------->unknown
     * SERIAL--------->a4a0d854
     * TAGS--------->release-keys
     * TIME--------->1440624955000
     * TYPE--------->user
     * UNKNOWN--------->unknown
     * USER--------->builder
     * getRadioVersion()--------->MPSS.DI.3.0-d354d78
     */

    //返回没有空格的小写商标字符串
    public static String getOSBrand(){
        return Build.BRAND.toLowerCase().replace(" ","");
    }
}
