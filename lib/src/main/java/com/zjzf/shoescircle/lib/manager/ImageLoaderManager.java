package com.zjzf.shoescircle.lib.manager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zjzf.shoescircle.lib.R;
import com.zjzf.shoescircle.lib.base.AppContext;
import com.zjzf.shoescircle.lib.other.RoundedCornersTransformation;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.widget.imageview.RoundedDrawable;

import java.io.File;

/**
 * Created by 陈志远 on 2017/5/2.
 * <p>
 * 图片加载单例
 */

public enum ImageLoaderManager {
    INSTANCE;
    private final GlideDsipatcher DISPATCHER = new GlideDsipatcher();

    private static RequestOptions DEFAULT_REQUEST_OPTIONS = new RequestOptions().centerCrop().placeholder(R.drawable.ic_error).error(R.drawable.ic_error);

    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    public void loadImage(ImageView target, Object from) {
        loadImage(target, from, R.drawable.ic_error);
    }

    public void loadImage(ImageView target, Object from, @DrawableRes int errorImage) {
        loadImage(target, from, errorImage, errorImage);
    }

    public void loadImage(ImageView target, Object from, @DrawableRes int loadingImg, @DrawableRes int errorImage) {
        loadImage(target, from, 0, 0, loadingImg, errorImage);
    }

    public void loadImage(ImageView target, Object from, int width, int hegith, @DrawableRes int loadingImg, @DrawableRes int errorImage) {
        RequestOptions options = DEFAULT_REQUEST_OPTIONS
                .autoClone()
                .placeholder(loadingImg).error(errorImage);
        if (width != 0 && hegith != 0) {
            options = options.override(width, hegith);
        }
        DISPATCHER.getGlide(target, from).apply(options).into(target);
    }

    public void loadImage(ImageView target, String from, @DrawableRes int loadingImg, @DrawableRes int errorImage, boolean cache) {
        RequestOptions options = DEFAULT_REQUEST_OPTIONS
                .autoClone()
                .placeholder(loadingImg)
                .error(errorImage)
                .skipMemoryCache(!cache)
                .diskCacheStrategy(cache ? DiskCacheStrategy.AUTOMATIC : DiskCacheStrategy.NONE);
        DISPATCHER.getGlide(target, from).apply(options).into(target);
    }

    public void loadGif(ImageView target, @DrawableRes int gifId) {
        DISPATCHER.getGlide(target, gifId).apply(DEFAULT_REQUEST_OPTIONS.autoClone()).into(target);
    }

    public void loadCircleImage(final ImageView target, Object from) {
        RequestOptions options = DEFAULT_REQUEST_OPTIONS.autoClone().error(getErrorDrawable(true));
        DISPATCHER.getGlide(target, from)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        target.setImageDrawable(createRoundDrawable(resource, 0, true));
                    }
                });
    }

    public Drawable getErrorDrawable(boolean isCircular) {
        if (isCircular) {
            return RoundedDrawable.fromDrawable(AppContext.getResources().getDrawable(R.drawable.ic_error), 0, 0, ColorStateList.valueOf(Color.TRANSPARENT), true);
        } else {
            return AppContext.getResources().getDrawable(R.drawable.ic_error);
        }
    }

    public void loadRoundImage(ImageView target, Object from, @Px int radius) {
        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_error).error(R.drawable.ic_error).transform(new RoundedCornersTransformation(radius, 0));
        DISPATCHER.getGlide(target, from).apply(options).into(target);
    }

    /**
     * 加载圆角图片
     *
     * @param target
     * @param from
     * @param loadingImg
     * @param errorImage
     * @param radius
     */
    public void loadRoundImage(ImageView target, Object from, @DrawableRes int loadingImg, @DrawableRes int errorImage, @Px int radius) {
        RequestOptions options = new RequestOptions().placeholder(loadingImg).error(errorImage).transform(new RoundedCornersTransformation(radius, 0));
        DISPATCHER.getGlide(target, from).apply(options).into(target);
    }

    public void loadRoundImage(ImageView target, Object from, @DrawableRes int loadingImg, @DrawableRes int errorImage, RoundedCornersTransformation.CornerType type, @Px int radius) {
        RequestOptions options = new RequestOptions().placeholder(loadingImg).error(errorImage).transform(new RoundedCornersTransformation(radius, 0, type));
        DISPATCHER.getGlide(target, from).apply(options).into(target);
    }

    public void loadBitmap(Context context, String imageUrl, final OnLoadBitmapListener listener) {
        LogHelper.trace("url=" + imageUrl);
        RequestOptions options = DEFAULT_REQUEST_OPTIONS.autoClone().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false);
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (listener != null) {
                            listener.onFailed(new IllegalArgumentException("download failed"));
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (listener != null) {
                            listener.onSuccess(resource);
                        }
                    }
                });
    }

    public interface OnLoadBitmapListener {
        void onSuccess(Bitmap bitmap);

        void onFailed(Exception e);
    }

    private Drawable createRoundDrawable(Drawable drawable, @Px int radius, boolean isCircular) {
        if (drawable == null) return null;
        return RoundedDrawable.fromDrawable(drawable, radius, 0, null, isCircular);
    }

    private Drawable createRoundDrawable(Bitmap bm, @Px int radius, boolean isCircular) {
        if (bm == null) return null;
        RoundedBitmapDrawable result =
                RoundedBitmapDrawableFactory.create(AppContext.getResources(), bm);
        if (isCircular) {
            result.setCircular(true);
        } else {
            result.setCornerRadius(radius);
        }
        return result;
    }

    private class GlideDsipatcher {

        public RequestBuilder getGlide(ImageView iv, Object o) {
            RequestManager manager = Glide.with(AppContext.getAppContext());
            if (o instanceof String) {
                return getGlideString(manager, (String) o, iv);
            } else if (o instanceof Integer) {
                return getGlideInteger(manager, (Integer) o, iv);
            } else if (o instanceof Uri) {
                return getGlideUri(manager, (Uri) o, iv);
            } else if (o instanceof File) {
                return getGlideFile(manager, (File) o, iv);
            }
            return getGlideString(manager, "", iv);
        }

        private RequestBuilder getGlideString(RequestManager manager, String str, ImageView iv) {
            return manager.load(str);
        }

        private RequestBuilder getGlideInteger(RequestManager manager, int source, ImageView iv) {
            return manager.load(source);
        }

        private RequestBuilder getGlideUri(RequestManager manager, Uri uri, ImageView iv) {
            return manager.load(uri);
        }

        private RequestBuilder getGlideFile(RequestManager manager, File file, ImageView iv) {
            return manager.load(file);
        }
    }

    public static abstract class OnLoadBitmapListenerAdapter implements OnLoadBitmapListener {

        @Override
        public void onSuccess(Bitmap bitmap) {

        }

        @Override
        public void onFailed(Exception e) {

        }
    }

}
