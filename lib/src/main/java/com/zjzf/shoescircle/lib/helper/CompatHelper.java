package com.zjzf.shoescircle.lib.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.zjzf.shoescircle.lib.base.AppContext;
import com.zjzf.shoescircle.lib.utils.LogHelper;

import java.io.File;


/**
 * Created by 陈志远 on 2017/6/15.
 * <p>
 * 适配helper
 */

public class CompatHelper {

    public static String PROVIDER_AUTH = "com.zjzf.shoescircleandroid";

    public static void init(String providerAuth) {
        PROVIDER_AUTH = providerAuth;
    }

    public static Uri getUri(@NonNull File file) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (TextUtils.isEmpty(PROVIDER_AUTH)) {
                    LogHelper.trace(LogHelper.e,"need init PROVIDER_AUTH");
                    return null;
                }
                return FileProvider.getUriForFile(AppContext.getAppContext(), PROVIDER_AUTH, file);
            } else {
                return Uri.fromFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    public static boolean isOverM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isOverN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

}
