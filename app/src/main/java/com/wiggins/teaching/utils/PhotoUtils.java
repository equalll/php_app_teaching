package com.wiggins.teaching.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.lib.base.util.FileUtils;
import com.lib.base.util.ImageUtils;
import com.orhanobut.logger.Logger;
import com.wiggins.teaching.app.App;
import com.wiggins.teaching.appconstant.AppConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <pre>
 *     author : Jimmy.tsang
 *     e-mail : jimmytsangfly@gmail.com
 *     time   : 2017/05/31
 *     desc   :  照相工具类
 *     version: 1.0
 * </pre>
 */
public class PhotoUtils {
    /**
     * 调用手机系统拍照请求码
     */
    public static final int REQUEST_CODE_CAMERA = 1001;

    /**
     * 打卡手机相册请求码
     */
    public static final int REQUEST_CODE_SELECT_FILE = 1002;

    /**
     * 剪裁
     */
    public final static int REQUEST_CODE_CROP = 1003;

    private static final int PROFILE_IMG_W = 500;
    private static final int PROFILE_IMG_H = 500;

    /**
     * 调用系统照相机拍照成功后存放的Uri
     */
    private Uri mCameraUri;

    private static final PhotoUtils sPhotoUtils = new PhotoUtils();


    /**
     * 照相成功后需要保存的图片
     */
    public static String CROP_RESULT_DIR = App.getAppContext().getExternalCacheDir() + "/crop_result.jpg";

    /**
     * 照片裁剪后保存的图片路径
     */
    public static String CLIPPING_SAVE_PATH = App.getAppContext().getExternalCacheDir() + "/cut_result.jpg";

    private PhotoUtils() {

    }

    public static PhotoUtils getInstance() {
        return sPhotoUtils;
    }


    /**
     * 获取相机Intent
     */
    private Intent getCameraIntent(Context context) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri;
        File file = new File(CROP_RESULT_DIR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 如果android 手机系统为7.0的话,如果直接使用本地真实路径被认为是不安全的
            //会抛出FileUriExposedException 异常，所以使用FileProvider进行处理
            // 第二参数可以是任意的字符串
            imageUri = FileProvider.getUriForFile(context, FileUtils.getApplicationId(context) + ".provider", file);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        } else {
            imageUri = Uri.fromFile(file);
        }
        mCameraUri = imageUri;
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        return cameraIntent;
    }

    /**
     * @return 获取拍照成功后的Uri
     */
    public Uri getUri() {
        return mCameraUri;
    }

    /**
     * 打开系统照相机
     */
    public void openCamera(Activity activity) {
        Intent takePictureIntent = getCameraIntent(activity);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
        }
    }

    /**
     * 打开系统相册
     */
    public void openAlbum(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //  intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
//            Uri uriForFile = FileProvider.getUriForFile(activity, FileUtils.getApplicationId(activity) + ".provider", new File(CROP_RESULT_DIR));
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
        } else {
            activity.startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
        }
    }

    /**
     * 这个方法只有在4.4及以上系统执行
     * 因为4.4 及以上系统的手机通过相册选中的图片不在返回真实的Uri
     * 所以要通过getImagePath() 这个方法来解析
     */
    @TargetApi(19)
    public String handleImageOnKitkat(Activity activity, Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(activity, uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];     //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(activity, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果不是document类型的Uri，则使用普通方式处理
            imagePath = getImagePath(activity, uri, null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是 file 类型的 Uri， 直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return imagePath;
    }


    public String getImagePath(Activity activity, Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = activity.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void crop(Activity activity, File file) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri outputUri = Uri.fromFile(new File(CLIPPING_SAVE_PATH));//存放裁剪后图片的 Uri
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(activity,
                    FileUtils.getApplicationId(activity) + ".provider",
                    file);//通过FileProvider创建一个content类型的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(file);
        }
        Logger.d("-------------outputUri = " + outputUri +"    imageUri = " + imageUri);
        intent.setDataAndType(imageUri, "image/*");
        //crop = true是设置在开启Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片的宽高
        // 为了防止有些手机裁剪的时候直接奔溃
        intent.putExtra("outputX", PROFILE_IMG_W);
        intent.putExtra("outputY", PROFILE_IMG_H);
        //  是否去除面部检测， 如果你需要特定的比例去裁剪图片，那么这个一定要去掉，因为它会破坏掉特定的比例
        intent.putExtra("noFaceDetection", true);
        //  是否需要保持比例
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        activity.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    /**
     * 裁剪成功后
     * @param path 裁剪后的图片路径
     */
    private void cropSuccess(final Context context, final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(path);//裁剪后的图片
                if (file.exists()) {
                    Bitmap cropBitmap;
                    InputStream inputStream = null;
                    try {
                        File cropFile = new File(PhotoUtils.CROP_RESULT_DIR);//压缩后要保存的路径
                        inputStream = context.getContentResolver().openInputStream(Uri.fromFile(file));
                        cropBitmap = BitmapFactory.decodeStream(inputStream);
                        OutputStream outStream = new FileOutputStream(cropFile);
                        cropBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        // 压缩图片
                        File compress = ImageUtils.compressImage(cropFile, App.getAppContext().getExternalCacheDir(),
                                AppConstant.IMG_QUALITY, false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (null != inputStream) {
                                inputStream.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }


}
