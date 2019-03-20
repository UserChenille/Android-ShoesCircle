package com.zjzf.shoescircleandroid.ui.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.OnRecyclerViewItemClickListener;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.helper.PhotoHelper;
import com.zjzf.shoescircle.ui.widget.GridSpacingItemDecoration;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.widget.popup.PopupSelectPhoto;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 陈志远 on 2018/8/27.
 */
public class FeedbackActivity extends BaseActivity {
    private static final String sAddPhotoTag = "addPhoto";
    @BindView(R.id.ed_input)
    EditText mEdInput;
    @BindView(R.id.rv_photo)
    RecyclerView mRvPhoto;
    @BindView(R.id.ed_contact)
    EditText mEdContact;
    @BindView(R.id.btn_apply)
    ExButton mBtnApply;

    private InnerPhotoAdapter mInnerPhotoAdapter;
    private PopupSelectPhoto mPopupSelectPhoto;


    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        mRvPhoto.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRvPhoto.addItemDecoration(new GridSpacingItemDecoration(3, UIHelper.dip2px(8), false));
        mInnerPhotoAdapter = new InnerPhotoAdapter(getContext());
        mInnerPhotoAdapter.addPhoto(sAddPhotoTag);
        mInnerPhotoAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(View v, int position, String data) {
                if (TextUtils.equals(data, sAddPhotoTag)) {
                    selectPhoto();
                }
            }
        });
        mRvPhoto.setAdapter(mInnerPhotoAdapter);

    }

    private void selectPhoto() {
        if (mPopupSelectPhoto == null) {
            mPopupSelectPhoto = new PopupSelectPhoto(getContext());
            mPopupSelectPhoto.setOnSelectPhotoMenuClickListener(new PopupSelectPhoto.OnSelectPhotoMenuClickListener() {
                @Override
                public void onShootClick() {
                    PhotoHelper.fromCamera(getContext());
                }

                @Override
                public void onAlbumClick() {
                    PhotoHelper.fromAlbum(getContext());
                }
            });
        }
        mPopupSelectPhoto.showPopupWindow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoHelper.handleActivityResult(getContext(), requestCode, resultCode, data, mPhotoCallback);
    }

    private PhotoHelper.PhotoCallback mPhotoCallback = new PhotoHelper.PhotoCallback() {
        @Override
        public void onFinish(String filePath) {
            mInnerPhotoAdapter.addPhoto(filePath);
        }

        @Override
        public void onError(String msg) {
            UIHelper.toast(msg);
        }
    };

    private class InnerPhotoAdapter extends BaseRecyclerViewAdapter<String> {

        public InnerPhotoAdapter(@NonNull Context context) {
            super(context);
        }

        @Override
        protected int getViewType(int position, @NonNull String data) {
            return 0;
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_select_photo;
        }

        @Override
        protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
            return new InnerViewHolder(rootView, viewType);
        }

        public void addPhoto(String photo) {
            List<String> data = getDatas();
            int index = data.indexOf(sAddPhotoTag);
            if (index < 0) {
                addData(sAddPhotoTag);
            } else {
                addData(0, photo);
            }
        }
    }

    private class InnerViewHolder extends BaseRecyclerViewHolder<String> {
        ImageView mImageView;
        ImageView mClose;

        public InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            mImageView = findViewById(R.id.iv_photo);
            mClose = findViewById(R.id.iv_delete);
        }

        @Override
        public void onBindData(final String data, int position) {
            if (TextUtils.equals(data, sAddPhotoTag)) {
                mImageView.setImageResource(R.drawable.ic_add_photo);
                mClose.setVisibility(View.GONE);
            } else {
                ImageLoaderManager.INSTANCE.loadImage(mImageView, data);
                mClose.setVisibility(View.VISIBLE);
            }
            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mInnerPhotoAdapter.remove(data);
                }
            });
        }
    }

}
