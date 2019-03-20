package com.zjzf.shoescircleandroid.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.zjzf.shoescircleandroid.model.CommentInfo;
import com.zjzf.shoescircleandroid.model.PopupCommentInfo;

import java.util.List;

/**
 * Created by 陈志远 on 2019/3/13.
 * https://www.rongcloud.cn/docs/android.html#listener_conversation_List_behavior
 */
public class IMViewModel extends ViewModel {
    private PopupCommentInfo mOtherCommentInfo;
    private PopupCommentInfo mMyCommentInfo;
    private List<CommentInfo> mCommentInfos;

    public PopupCommentInfo getOtherCommentInfo() {
        return mOtherCommentInfo;
    }

    public IMViewModel setOtherCommentInfo(PopupCommentInfo otherCommentInfo) {
        mOtherCommentInfo = otherCommentInfo;
        return this;
    }

    public PopupCommentInfo getMyCommentInfo() {
        return mMyCommentInfo;
    }

    public IMViewModel setMyCommentInfo(PopupCommentInfo myCommentInfo) {
        mMyCommentInfo = myCommentInfo;
        return this;
    }

    public List<CommentInfo> getCommentInfos() {
        return mCommentInfos;
    }

    public IMViewModel setCommentInfos(List<CommentInfo> commentInfos) {
        mCommentInfos = commentInfos;
        return this;
    }
}
