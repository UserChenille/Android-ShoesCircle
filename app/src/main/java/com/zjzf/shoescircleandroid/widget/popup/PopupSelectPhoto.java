package com.zjzf.shoescircleandroid.widget.popup;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;

import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircleandroid.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2017/3/2.
 * <p>
 * 选择照片popup
 */

public class PopupSelectPhoto extends BasePopupWindow implements View.OnClickListener {

    private View shoot;
    private View album;
    private View cancel;

    private OnSelectPhotoMenuClickListener listener;

    public PopupSelectPhoto(Activity context) {
        super(context);
        setBlurBackgroundEnable(true)
                .setPopupWindowFullScreen(true);

        shoot = findViewById(R.id.shoot);
        album = findViewById(R.id.album);
        cancel = findViewById(R.id.cancel);

        ViewUtil.setViewsClickListener(this, shoot, album, cancel);
        setBlurBackgroundEnable(true);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    public OnSelectPhotoMenuClickListener getOnSelectPhotoMenuClickListener() {
        return listener;
    }

    public PopupSelectPhoto setOnSelectPhotoMenuClickListener(OnSelectPhotoMenuClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.shoot) {
            if (listener != null) {
                listener.onShootClick();
            }
            dismissWithOutAnimate();

        } else if (i == R.id.album) {
            if (listener != null) {
                listener.onAlbumClick();
            }
            dismissWithOutAnimate();

        } else if (i == R.id.cancel) {
            dismiss();

        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_select_photo);
    }

    public interface OnSelectPhotoMenuClickListener {
        void onShootClick();

        void onAlbumClick();
    }
}
