package com.zjzf.shoescircleandroid.ui.express;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.widget.UnderLineLinearLayout;
import com.zjzf.shoescircle.ui.widget.textview.ExTextView;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.net.apis.ExpressApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.model.express.ExpressCompanyInfo;
import com.zjzf.shoescircleandroid.model.express.ExpressInfo;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 陈志远 on 2019/3/11.
 */
public class ExpressDetailActivity extends BaseActivity {
    @BindView(R.id.tv_express_name)
    ExTextView mTvExpressName;
    @BindView(R.id.tv_express_id)
    ExTextView mTvExpressId;
    @BindView(R.id.layout_detail)
    UnderLineLinearLayout mLayoutDetail;
    private Option mOption;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_express_detail;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {
        mOption = getActivityOption(Option.class);
        if (mOption == null) finish();
    }

    @Override
    protected void onInitView(View decorView) {
        if (mOption == null) {
            finish();
            return;
        }
        RetrofitClient.get()
                .create(ExpressApis.class)
                .queryExpress(mOption.expressCode, mOption.expressId)
                .compose(this.<ObjectData<List<ExpressInfo>>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<List<ExpressInfo>>>() {
                    @Override
                    public void onNext(ObjectData<List<ExpressInfo>> result) {
                        if (!ToolUtil.isListEmpty(result.getData())) {
                            addDetailView(result.getData());
                        } else {
                            addEmptyView();
                        }
                    }
                });
        mTvExpressName.setLoadingText("加载中...");
        MultiSpanUtil.create("物流单号：" + mOption.expressId)
                .append(mOption.expressId).setTextColorFromRes(R.color.color_black1)
                .into(mTvExpressId);
        RetrofitClient.get()
                .create(ExpressApis.class)
                .queryExpressCompany(mOption.expressId)
                .compose(this.<ObjectData<List<ExpressCompanyInfo>>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<List<ExpressCompanyInfo>>>() {
                    @Override
                    public void onNext(ObjectData<List<ExpressCompanyInfo>> result) {
                        if (!ToolUtil.isListEmpty(result.getData())) {
                            ExpressCompanyInfo info = result.getData().get(0);
                            MultiSpanUtil.create("物流公司：" + info.getName())
                                    .append(info.getName()).setTextColorFromRes(R.color.color_black1)
                                    .into(mTvExpressName);
                        }
                    }
                });
    }

    void addEmptyView() {
        TextView textView = new TextView(getContext());
        textView.setPadding(UIHelper.dip2px(16),
                UIHelper.dip2px(16),
                UIHelper.dip2px(16),
                UIHelper.dip2px(16));
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(UIHelper.sp2px(14));
        textView.setTextColor(UIHelper.getColor(R.color.color_black2));
        mLayoutDetail.removeAllViewsInLayout();
        mLayoutDetail.addView(textView);
    }

    void addDetailView(List<ExpressInfo> mDatas) {
        for (int i = 0; i < mDatas.size(); i++) {
            mLayoutDetail.addViewPreventLayout(generateView(mDatas.get(i), i == 0));
        }
        mLayoutDetail.requestLayout();
    }

    View generateView(ExpressInfo expressInfo, boolean isFirst) {
        View v = View.inflate(this, R.layout.item_express_detail, null);

        TextView tvTitle = v.findViewById(R.id.tv_express_desc);
        TextView tvDate = v.findViewById(R.id.tv_express_date);
        if (isFirst) {
            tvTitle.setTextColor(Color.parseColor("#FF434D"));
            tvDate.setTextColor(Color.parseColor("#FF434D"));
        }
        tvTitle.setText(expressInfo.getStatusDescription());
        tvDate.setText(expressInfo.getDate());
        return v;
    }


    public static class Option extends BaseActivityOption<Option> {
        private String expressCode;
        private String expressId;

        public Option setExpressCode(String expressCode) {
            this.expressCode = expressCode;
            return this;
        }

        public Option setExpressId(String expressId) {
            this.expressId = expressId;
            return this;
        }
    }
}
