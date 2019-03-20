package com.zjzf.shoescircle.ui.helper;

import android.content.Context;
import android.graphics.Color;

import com.bigkoo.pickerview.TimePickerView;

import java.util.Calendar;

/**
 * Created by 陈志远 on 2017/11/24.
 * <p>
 * 时间选择
 * <p>
 * https://github.com/Bigkoo/Android-PickerView
 */

public class PickerHelper {

    public static void showTimePicker(Context context, long curTimeInMillis, TimePickerView.OnTimeSelectListener onTimeSelectListener) {
        Calendar c = Calendar.getInstance();
        int thisYear = c.get(Calendar.YEAR);
        int startYear = thisYear - 71;
        int endYear = thisYear - 18;
        TimePickerView pvTime = new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, false, false, false})//只显示到年月日
                .setSubmitColor(Color.parseColor("#EA544A"))//确定按钮文字颜色
                .setTitleText("请选择日期")
                .setRange(startYear, endYear)
                .setLabel("年", "月", "日", "", "", "")//默认设置为年月日时分秒
                .build();
        if (curTimeInMillis != 0) {
            c.setTimeInMillis(curTimeInMillis);
        }
        pvTime.setDate(c);//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }
}
