package com.zjzf.shoescircleandroid.ui.im.message;

import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.utils.DecimalUtil;
import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.model.im.TransactionMessageContent;
import com.zjzf.shoescircleandroid.ui.transaction.TransactionApplyActivity;
import com.zjzf.shoescircleandroid.ui.transaction.TransactionOrderDetailActivity;

import java.util.Locale;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by 陈志远 on 2018/12/9.
 * https://www.rongcloud.cn/docs/android.html#message_customize
 */
@ProviderTag(messageContent = TransactionMessage.class, showProgress = true)
public class TransactionMessageProvider extends IContainerItemProvider.MessageProvider<TransactionMessage> {

    static class ViewHolder {
        public View rootView;
        public TextView tvName;
        public TextView tvPrice;
        public TextView tvProductNo;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name);
            this.tvPrice = (TextView) rootView.findViewById(R.id.tv_price);
            this.tvProductNo = (TextView) rootView.findViewById(R.id.tv_product_no);
        }

    }

    @Override
    public void bindView(View view, int i, TransactionMessage transactionMessage, UIMessage uiMessage) {
        ViewHolder vh = (ViewHolder) view.getTag();

        TransactionMessageContent content = transactionMessage.getContent();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
//            vh.tvName.setText(String.format("向%1$s发起收款", uiMessage.getUserInfo().getName()));
            vh.tvName.setText(transactionMessage.getContent().getName());
            vh.rootView.setBackgroundResource(R.drawable.rc_ic_bubble_right_file);
        } else {
//            vh.tvName.setText(String.format("%1$s向你发起收款", uiMessage.getUserInfo().getName()));
            vh.tvName.setText(transactionMessage.getContent().getName());
            vh.rootView.setBackgroundResource(R.drawable.rc_ic_bubble_left);
        }


        String amount = DecimalUtil.format(StringUtil.toDouble(content.getOrderAmount()));
        MultiSpanUtil.create(amount + "元")
                .append(amount).setTextSize(20)
                .into(vh.tvPrice);
        vh.tvProductNo.setText(String.format(Locale.getDefault(), "货号%s%s双", content.getFreightNo(), StringUtil.formatInteger(content.getNum())));
    }

    @Override
    public Spannable getContentSummary(TransactionMessage transactionMessage) {
        return null;
    }

    @Override
    public void onItemClick(View view, int i, TransactionMessage transactionMessage, UIMessage uiMessage) {
        if (transactionMessage == null || transactionMessage.getContent() == null) return;
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            ActivityLauncher.startToTransactionOrderDetailActivity(view.getContext(), new TransactionOrderDetailActivity.Option()
                    .setMessageWrapper(TransactionMessageWrapper.createFrom(transactionMessage.getContent())));
        } else {
            ActivityLauncher.startToTransactionApplyActivity(view.getContext(), new TransactionApplyActivity.Option()
                    .setOrderId(transactionMessage.getContent().getOrderId()));
        }
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_message_transaction, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(vh);
        return v;
    }


}
