package com.lib.base.net;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by jarylan on 2017/3/22.
 * 封装请求体
 * 项目中拓展请继承此类
 */
public class BaseHttpRequestParam {

    /**
     *封装解析字符串的 body
     * @param value
     * @return
     */
    protected static RequestBody parseRequestBody(String value) {
        return RequestBody.create(MediaType. parse("text/plain"), value);
    }

    /**
     *封装解析字符串的 body
     * @param value
     * @return
     */
    protected static RequestBody parsePutRequestBody(String value) {
        return RequestBody.create(MediaType. parse("application/x-www-form-urlencoded"), value);
    }

    /**
     *封装解析 int 的 body ;
     * @param value
     * @return
     */
    protected static RequestBody parseRequestBody(int value) {
        return RequestBody.create(MediaType. parse("text/plain"), value+"");
    }

    /**
     * 封装解析图片的 body
     * 示例：map.put(parseImageMapKey("服务器定的图片 key", file.getName()), parseImageRequestBody(file));
     * @param file
     * @return
     */
    protected static RequestBody parseImageRequestBody(File file) {
        return RequestBody.create(MediaType. parse("image/*"), file);
    }

    /**
     * 封装解析文件的 body
     * 示例：map.put(parseImageMapKey("服务器定的文件 key", file.getName()), parseFileRequestBody(file));
     *@param file
     * @return
     */
    protected static RequestBody parseFileRequestBody(File file){
        return RequestBody.create(MediaType. parse("multipart/form-data"), file);
    }

    /**
     * 封装解析文件 body 的 key
     * @param key
     * @param fileName
     * @return
     */
    protected static String parseImageMapKey(String key, String fileName) {
        return key + "\"; filename=\"" + fileName;
    }
}
