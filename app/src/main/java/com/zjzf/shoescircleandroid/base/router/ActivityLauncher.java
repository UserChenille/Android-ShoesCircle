package com.zjzf.shoescircleandroid.base.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.ui.express.ExpressDetailActivity;
import com.zjzf.shoescircleandroid.ui.goods.GoodsDetailActivity;
import com.zjzf.shoescircleandroid.ui.goods.PostGoodsActivity;
import com.zjzf.shoescircleandroid.ui.login.BindPhoneActivity;
import com.zjzf.shoescircleandroid.ui.login.LoginActivity;
import com.zjzf.shoescircleandroid.ui.main.MainActivity;
import com.zjzf.shoescircleandroid.ui.me.MyGoodsDemandActivity;
import com.zjzf.shoescircleandroid.ui.me.MyGoodsSellActivity;
import com.zjzf.shoescircleandroid.ui.me.UserEditActivity;
import com.zjzf.shoescircleandroid.ui.other.BindAliPayActivity;
import com.zjzf.shoescircleandroid.ui.other.FeedbackActivity;
import com.zjzf.shoescircleandroid.ui.search.SearchGoodsActivity;
import com.zjzf.shoescircleandroid.ui.transaction.OpenTransactionActivity;
import com.zjzf.shoescircleandroid.ui.transaction.scan.ExpressCompanySelectActivity;
import com.zjzf.shoescircleandroid.ui.transaction.scan.QRScanActivity;
import com.zjzf.shoescircleandroid.ui.transaction.scan.QRScanResultOrEditActivity;
import com.zjzf.shoescircleandroid.ui.transaction.TransactionApplyActivity;
import com.zjzf.shoescircleandroid.ui.transaction.TransactionDetailActivity;
import com.zjzf.shoescircleandroid.ui.transaction.TransactionOrderDetailActivity;

import static com.zjzf.shoescircleandroid.base.BaseActivity.INTENT_ACTIVITY_OPTION;


/**
 * Created by 陈志远 on 2018/7/22.
 */
public class ActivityLauncher {
    private static boolean isContextNull(Context context) {
        return context == null;
    }

    public static void startToLoginActivity(Context context) {
        if (isContextNull(context)) return;
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void startToBindPhoneActivity(Context context) {
        if (isContextNull(context)) return;
        context.startActivity(new Intent(context, BindPhoneActivity.class));
    }

    public static void startToMainActivity(Context context) {
        startToMainActivity(context, true);
    }

    public static void startToMainActivity(Context context, boolean clearTop) {
        if (isContextNull(context)) return;
        Intent intent = new Intent(context, MainActivity.class);
        if (clearTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    public static void startToGoodsDetailActivity(Context context, GoodsDetailActivity.GoodsDetailOption option) {
        if (isContextNull(context)) return;
        startWithOption(context, GoodsDetailActivity.class, option);
    }

    public static void startToFeedbackActivity(Context context) {
        if (isContextNull(context)) return;
        context.startActivity(new Intent(context, FeedbackActivity.class));
    }

    public static void startToSearchActivity(Context context) {
        if (isContextNull(context)) return;
        context.startActivity(new Intent(context, SearchGoodsActivity.class));
    }

    public static void startToUserEditActivity(Context context) {
        if (isContextNull(context)) return;
        context.startActivity(new Intent(context, UserEditActivity.class));
    }

    public static void startToPostGoodsActivity(Context context) {
        if (isContextNull(context)) return;
        context.startActivity(new Intent(context, PostGoodsActivity.class));

    }

    public static void startToGoodsDemandActivity(Context context, MyGoodsDemandActivity.Option option) {
        startWithOption(context, MyGoodsDemandActivity.class, option);
    }

    public static void startToGoodsSellActivity(Context context, MyGoodsSellActivity.Option option) {
        startWithOption(context, MyGoodsSellActivity.class, option);
    }

    public static void startToQRCodeScanActivity(Object context, QRScanActivity.Option option) {
        startWithOption(context, QRScanActivity.class, option);
        if (context instanceof Activity) {
            transitionVerticalIn(((Activity) context));
        } else if (context instanceof Fragment) {
            transitionVerticalIn(((Fragment) context).getContext());
        }
    }

    public static void startToQRCodeScanResultOrEditActivity(Context context, QRScanResultOrEditActivity.Option option) {
        startWithOption(context, QRScanResultOrEditActivity.class, option.setRequestCode(1001));
    }


    public static void startToOpenTransactionActivity(Context context, OpenTransactionActivity.Option option) {
        startWithOption(context, OpenTransactionActivity.class, option);
    }

    public static void startToTransactionDetailActivity(Context context, TransactionDetailActivity.Option option) {
        startWithOption(context, TransactionDetailActivity.class, option);
    }

    public static void startToExpressDetailActivity(Context context, ExpressDetailActivity.Option option) {
        startWithOption(context, ExpressDetailActivity.class, option);
    }

    public static void startToTransactionApplyActivity(Object context, TransactionApplyActivity.Option option) {
        startWithOption(context, TransactionApplyActivity.class, option);
    }

    public static void startToTransactionOrderDetailActivity(Context context, TransactionOrderDetailActivity.Option option) {
        startWithOption(context, TransactionOrderDetailActivity.class, option);
    }

    public static void startToBindAliPayActivity(Fragment context, int requestCode) {
        startWithOption(context, BindAliPayActivity.class, new BindAliPayActivity.Option().setRequestCode(requestCode));
    }

    public static void startToBindAliPayActivity(Activity context, int requestCode) {
        startWithOption(context, BindAliPayActivity.class, new BindAliPayActivity.Option().setRequestCode(requestCode));
    }

    public static void startToExpressCompanySelectActivity(Context context, int requestCode) {
        startWithOption(context, ExpressCompanySelectActivity.class, new BaseActivityOption().setRequestCode(requestCode));
    }

    //------------------------------------------animate-----------------------------------------------
    public static void transitionVerticalIn(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_translate);
    }

    public static void transitionFadeIn(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.no_translate);
    }

    public static void transitionHorizontalIn(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.no_translate);
    }

    public static void transitionVerticalOnFinish(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.no_translate, R.anim.slide_out_bottom);
    }


    public static void transitionFadeOnFinish(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.no_translate, R.anim.fade_out);
    }

    public static void transitionHorizontalOnFinish(Context context, boolean isLeft) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.no_translate, isLeft ? R.anim.slide_left_out : R.anim.slide_right_out);
    }

    public static void transitionVerticalWithAlphaIn(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.slide_in_bottom_with_alpha, R.anim.no_translate);
    }

    public static void alphaOut(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static void scaleFadeOut(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(0, R.anim.scale_fade_out);
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        if (fragmentManager == null || fragment == null) return;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded() && null == fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName())) {
            transaction.add(frameId, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.show(fragment);
        }
        transaction.commitAllowingStateLoss();
    }


    /**
     * 带有参数的 start activity
     */
    private static void startWithOption(Object which, Class<?> what, BaseActivityOption option) {
        if (!(which instanceof Activity) && !(which instanceof Context) && !(which instanceof Fragment))
            return;
        boolean isForResult = option != null && option.getRequestCode() != -1;
        if (which instanceof Activity) {
            if (isForResult) {
                ((Activity) which).startActivityForResult(new Intent(((Activity) which), what).putExtra(INTENT_ACTIVITY_OPTION, option), option.getRequestCode());
            } else {
                ((Activity) which).startActivity(new Intent(((Activity) which), what).putExtra(INTENT_ACTIVITY_OPTION, option));
            }
            return;
        }

        if (which instanceof Context) {
            ((Context) which).startActivity(new Intent(((Context) which), what).putExtra(INTENT_ACTIVITY_OPTION, option));
            return;
        }

        //fragment
        if (isForResult) {
            ((Fragment) which).startActivityForResult(new Intent(((Fragment) which).getContext(), what).putExtra(INTENT_ACTIVITY_OPTION, option), option.getRequestCode());
        } else {
            ((Fragment) which).startActivity(new Intent(((Fragment) which).getContext(), what).putExtra(INTENT_ACTIVITY_OPTION, option));
        }

    }

}
