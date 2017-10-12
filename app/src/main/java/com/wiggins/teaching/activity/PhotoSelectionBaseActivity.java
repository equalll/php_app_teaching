package com.wiggins.teaching.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.lib.base.util.ImageUtils;
import com.wiggins.teaching.R;
import com.wiggins.teaching.app.App;
import com.wiggins.teaching.appconstant.AppConstant;
import com.wiggins.teaching.ui.base.BaseActivity;
import com.wiggins.teaching.utils.PhotoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * <pre>
 *     author ：wiggins on 2017/7/13 16:04
 *     e-mail ：traywangjun@gmail.com
 *     time   : 2017/06/05
 *     desc   : 关于相册选择的 Activity 基类
 *     version: 1.0
 * </pre>
 */
public abstract class PhotoSelectionBaseActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private static final int COMPRESS_WHAT = 1005; //压缩

    /**
     * 照相
     */
    @AfterPermissionGranted(AppConstant.CAMERA_REQUEST_CODE)
    protected void camera() {
        //所要申请的权限
        String[] perms = {Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            PhotoUtils.getInstance().openCamera(this);
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this,"要允许新闻app 进行拍照和访问您设备上的照片、媒体内容和文件吗？",
                    AppConstant.CAMERA_REQUEST_CODE, perms);
        }
    }

    /**
     * 相册
     */
    @AfterPermissionGranted(AppConstant.OPEN_ALBUM_REQUEST_CODE)
    protected void openAlbum() {
        //所要申请的权限
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            PhotoUtils.getInstance().openAlbum(this);
        } else {
            EasyPermissions.requestPermissions(this,"要允许新闻app 进行拍照和访问您设备上的照片、媒体内容和文件吗？",
                    AppConstant.OPEN_ALBUM_REQUEST_CODE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(R.string.title_permissions_required)
                    .setRationale(R.string.msg_no_permissions).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoUtils.REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            //照相
            tailorPhoto();
        } else if (requestCode == PhotoUtils.REQUEST_CODE_SELECT_FILE && resultCode == RESULT_OK) {
            //相册
            tailorPhoto(data);
        } else if (requestCode == PhotoUtils.REQUEST_CODE_CROP && resultCode == RESULT_OK) {
            //裁剪成功后需要进行压缩
            cropSuccess(PhotoUtils.CLIPPING_SAVE_PATH);
        }
    }

    /**
     * 调用手机系统拍照功能成功后裁剪照片
     */
    private void tailorPhoto() {
        PhotoUtils.getInstance().crop(this, new File(PhotoUtils.CROP_RESULT_DIR));
    }

    /**
     * 调用手机系统相册选中照片后裁剪照片
     *
     * @param data data
     */
    private void tailorPhoto(Intent data) {
        // 1. 选完相册后，来到裁剪界面，这个时候直接挂，为什么？？
        PhotoUtils instance = PhotoUtils.getInstance();
        String path;

        if (Build.VERSION.SDK_INT >= 19) {
            //4.4及以上
            path = instance.handleImageOnKitkat(this, data);
        } else {
            //4.4 以下
            path = instance.getImagePath(this, data.getData(), null);
        }
        PhotoUtils.getInstance().crop(this, new File(path));
    }


    /**
     * 裁剪成功后
     */
    private void cropSuccess(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(path);
                if (file.exists()) {
                    Bitmap cropBitmap;
                    InputStream inputStream = null;
                    try {
                        File cropFile = new File(PhotoUtils.CROP_RESULT_DIR);
                        inputStream = getContentResolver().openInputStream(Uri.fromFile(file));
                        cropBitmap = BitmapFactory.decodeStream(inputStream);
                        OutputStream outStream = new FileOutputStream(cropFile);
                        cropBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        // 压缩图片
                        File compress = ImageUtils.compressImage(cropFile, App.getAppContext().getExternalCacheDir(),
                                AppConstant.IMG_QUALITY, false);
                        mHandler.sendMessage(mHandler.obtainMessage(COMPRESS_WHAT, compress));

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

    /**
     * 压缩后更新图片
     */
    protected void updatePic(File file) {

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case COMPRESS_WHAT:
                    if(!isFinishing()){
                        updatePic((File) msg.obj);
                    }
                    break;
                default:
                    break;
            }

            return false;
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

}
