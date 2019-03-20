package com.zjzf.shoescircleandroid.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.lib.widget.imageview.RoundedImageView;
import com.zjzf.shoescircle.ui.helper.PhotoHelper;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.model.UserDetailInfo;
import com.zjzf.shoescircleandroid.widget.popup.PopupSelectPhoto;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 陈志远 on 2018/8/30.
 */
public class UserEditActivity extends BaseActivity {
    @BindView(R.id.ll_avatar)
    LinearLayout mLlAvatar;
    @BindView(R.id.tv_nick)
    TextView mTvNick;
    @BindView(R.id.ll_nick)
    LinearLayout mLlNick;
    @BindView(R.id.tv_gender)
    TextView mTvGender;
    @BindView(R.id.ll_gender)
    LinearLayout mLlGender;
    @BindView(R.id.iv_avatar)
    RoundedImageView mIvAvatar;

    private PopupSelectPhoto mSelectPhoto;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_edit;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        UserInfoManager.INSTANCE.getUserInfo()
                .getUserDetailInfo(new SimpleCallback<UserDetailInfo>() {
                    @Override
                    public void onCall(UserDetailInfo data) {
                        ImageLoaderManager.INSTANCE.loadCircleImage(mIvAvatar, data.getAvatar());
                        mTvNick.setText(data.getMemName());
                    }
                }, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoHelper.handleActivityResult(this, requestCode, resultCode, data, new PhotoHelper.PhotoCallback() {
            @Override
            public void onFinish(String filePath) {
                ImageLoaderManager.INSTANCE.loadCircleImage(mIvAvatar, filePath);
            }

            @Override
            public void onError(String msg) {
                UIHelper.toast(msg);
            }
        });
    }

    @OnClick(R.id.ll_avatar)
    void selectAvatar() {
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

}
