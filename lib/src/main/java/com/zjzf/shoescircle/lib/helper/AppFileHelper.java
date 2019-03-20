package com.zjzf.shoescircle.lib.helper;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.zjzf.shoescircle.lib.utils.FileUtil;
import com.zjzf.shoescircle.lib.utils.LogHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 读取该路径的时候需要授予读写权限
 * {@link PermissionHelper}
 * Created by 陈志远 on 2018/3/2.
 */
public class AppFileHelper {
    private static final String TAG = "AppFileHelper";

    private static String storagePath;

    public static String ROOT_PATH = "com/zjzf/";
    public static String APP_PATH = "shoesCircle/";
    public static final String DOWNLOAD_PATH = "download/";
    public static final String MUSIC_PATH = "music/";
    public static final String PHOTO_PATH = "photo/";
    public static final String PIC_PATH = "image/";
    public static final String SHARE_PATH = "share/";
    public static final String ICON_PATH = "icon/";

    public static void init(Application application) {
        if (!TextUtils.isEmpty(storagePath)) return;
        storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (TextUtils.isEmpty(storagePath)) {
            storagePath = Environment.getDataDirectory().getAbsolutePath();
        }
        storagePath = FileUtil.checkFileSeparator(storagePath);
    }

    private static void checkAndMakeDirs(String... filePath) {
        for (String s : filePath) {
            checkAndMakeDir(s);
        }
    }

    private static void checkAndMakeDir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            boolean result = file.mkdirs();
            LogHelper.trace("mkdirs  >>>  " + file.getAbsolutePath() + "  success  >>  " + result);
        } else {
            LogHelper.trace("mkdirs  >>>  " + file.getAbsolutePath() + "已经存在");
        }
    }

    public static String getSdCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getAppRootDir() {
        checkAndMakeDir(storagePath.concat(ROOT_PATH));
        return storagePath.concat(ROOT_PATH);
    }

    public static String getPhotoDir() {
        checkAndMakeDir(storagePath.concat(ROOT_PATH + PHOTO_PATH));
        return storagePath.concat(ROOT_PATH + PHOTO_PATH);
    }

    public static String getDownloadDir() {
        LogHelper.trace("downloadDir=" + storagePath.concat(ROOT_PATH + DOWNLOAD_PATH));
        checkAndMakeDir(storagePath.concat(ROOT_PATH + DOWNLOAD_PATH));
        return storagePath.concat(ROOT_PATH + DOWNLOAD_PATH);
    }

    public static String getIconPath() {
        checkAndMakeDir(storagePath.concat(ROOT_PATH + ICON_PATH));
        return storagePath.concat(ROOT_PATH + ICON_PATH);
    }

    public static String getDownloadPath(String url) {
        String name = FileUtil.getFileName(url);
        return getDownloadDir() + name;
    }

    /**
     * 根据url截取文件名，不带后缀
     *
     * @param url
     * @return
     */
    private static String getWebNameByUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int start = url.lastIndexOf("/");
            if (start > 0) {
                int end = url.lastIndexOf(".");
                if (end > 0 && end > start) {
                    return url.substring(start, end);
                }
            }
        }
        return null;
    }

    public static String createShareImageName() {
        return createImageName(false);
    }

    public static String createImageName(boolean isJpg) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.US);
        return dateFormat.format(date) + (isJpg ? ".jpg" : ".png");
    }

    public static String createCropImageName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.US);
        return dateFormat.format(date) + "_crop.png";
    }

    public static String getLogDir() {
        String path = getAppRootDir() + "log/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String createLogFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.CHINA);
        return dateFormat.format(date) + ".txt";
    }

    private static void createNoMediaFile(String dir) {
        File file = new File(dir + ".nomedia");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getUpdateDir() {
        String path = getAppRootDir() + "update/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private static String getTakePhotoDir() {
        String path = getAppRootDir() + "camera/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static File getCrashLogDirFile() {
        String path = getAppRootDir() + "crash/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getLogDirFile() {
        String path = getAppRootDir() + "log/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String getTakePhotoPath() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss", Locale.US);
        String name = dateFormat.format(date) + ".tmp"; // 先以tmp作为后缀名，待用户选保存之后，再改成jpg从而生效

        return getTakePhotoDir() + name;
    }

    public static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static void showSDCardDisable(Context context, String title, String message, String ok) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title).setMessage(message)
                .setPositiveButton(ok, null);
        AlertDialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(true);
        dlg.show();
    }

    public static void sendScanBroadcast(Context context, String path) {
//        final MediaScannerConnection msc = new MediaScannerConnection(context,
//                new MediaScannerConnection.MediaScannerConnectionClient() {
//                    @Override
//                    public void onMediaScannerConnected() {
//                        scanFile("/sdcard/image.jpg", "image/jpeg");
//                    }
//
//                    @Override
//                    public void onScanCompleted(String s, Uri uri) {
//
//                    }
//                });
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(path)));
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(path));
        intent.setData(uri);
        context.sendBroadcast(intent);//这个广播的目的就是更新图库
    }
}
