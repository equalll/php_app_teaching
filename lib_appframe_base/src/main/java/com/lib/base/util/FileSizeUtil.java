package com.lib.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * <pre>
 *     author : Jimmy.tsang
 *     e-mail : jimmytsangfly@gmail.com
 *     time   : 2017/03/27
 *     desc   :  文件大小
 *     version: 1.0
 * </pre>
 */
public class FileSizeUtil {
    public static final int SIZE_TYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZE_TYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZE_TYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZE_TYPE_GB = 4;//获取文件大小单位为GB的double值

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file 指定获取文件的大小
     * @return 文件的大小
     * @throws Exception io
     */
    private static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小
     *
     * @param fileS 文件大小 long
     * @param minSize 限定最小单位
     * @return 带单位文件大小
     */
    public static String FormetFileSizeSpecifyMin(long fileS,int minSize) {
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSizeString;
        String minUnit = getSizeUnitString(minSize);
        String wrongSize = "0" + minUnit;
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS    fileS
     * @param sizeType sizeType
     * @return fileSizeLong
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZE_TYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZE_TYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZE_TYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZE_TYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 返回单位 String
     * @param unit 单位类型
     * @return
     */
    private static String getSizeUnitString(int unit){
        String unitString = "B";
        switch (unit){
            case SIZE_TYPE_B:
                unitString = "B";
                break;
            case SIZE_TYPE_KB:
                unitString = "KB";
                break;
            case SIZE_TYPE_MB:
                unitString = "MB";
                break;
            case SIZE_TYPE_GB:
                unitString = "GB";
                break;
        }
        return unitString;
    }
}
