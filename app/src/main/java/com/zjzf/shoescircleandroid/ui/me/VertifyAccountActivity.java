package com.zjzf.shoescircleandroid.ui.me;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.helper.PhotoHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.widget.popup.PopupSelectPhoto;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 陈志远 on 2019/2/17.
 */
public class VertifyAccountActivity extends BaseActivity {
    @BindView(R.id.iv_selected_photo)
    ImageView mIvSelectedPhoto;
    @BindView(R.id.tv_selected_photo)
    TextView mTvSelectedPhoto;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    @BindView(R.id.btn_confirm)
    ExButton mBtnConfirm;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vertify_account;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        MultiSpanUtil.create("请截取你的千牛网APP首页，截图时间与上传时间不能超过1分钟。")
                .append("千牛网APP首页").setTextColorFromRes(R.color.color_black1).setTextType(Typeface.DEFAULT_BOLD)
                .into(mTvTips);

    }

    private PopupSelectPhoto mSelectPhoto;

    @OnClick(R.id.tv_selected_photo)
    void selectPhoto() {
        if (mSelectPhoto == null) {
            mSelectPhoto = new PopupSelectPhoto(this);
            mSelectPhoto.setOnSelectPhotoMenuClickListener(new PopupSelectPhoto.OnSelectPhotoMenuClickListener() {
                @Override
                public void onShootClick() {
                    PhotoHelper.fromCamera(getContext(), true);
                }

                @Override
                public void onAlbumClick() {
                    PhotoHelper.fromAlbum(getContext(), true);
                }
            });
        }
        mSelectPhoto.showPopupWindow();
    }

    @OnClick(R.id.btn_confirm)
    void confirm(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoHelper.handleActivityResult(this, requestCode, resultCode, data, new PhotoHelper.PhotoCallback() {
            @Override
            public void onFinish(String filePath) {
                ViewUtil.setViewsVisible(View.GONE, mTvTips);
                ViewUtil.setViewsVisible(View.VISIBLE, mIvSelectedPhoto);
                ImageLoaderManager.INSTANCE.loadCircleImage(mIvSelectedPhoto, filePath);
            }

            @Override
            public void onError(String msg) {
                UIHelper.toast(msg);
            }
        });
    }

}
