package com.zjzf.shoescircle.ui.widget.edit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;

import com.zjzf.shoescircle.lib.utils.DecimalMathUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.uilib.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by 陈志远 on 2018/6/27.
 * <p>
 * 带action的edittext，默认是清除
 */
public class ExEditText extends EditText {
    private static final String TAG = "ExEditText";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FormatMode.NORMAL, FormatMode.PHONE})
    public @interface FormatMode {
        int NORMAL = 0;
        int PHONE = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ErrorTag.INPUT_EMPTY, ErrorTag.NOT_ENOUTH, ErrorTag.VALIDATE, ErrorTag.INVALIDATED_INCREASE})
    public @interface ErrorTag {
        int INPUT_EMPTY = -2;
        int NOT_ENOUTH = -1;
        int VALIDATE = 0;
        int INVALIDATED_INCREASE = 1;
    }

    @FormatMode
    private int formateMode = FormatMode.NORMAL;

    private static final int NORMAL_ACTION_CLEAR = -1;

    private int mActionDrawableGravity = Gravity.RIGHT;
    private Drawable mActionDrawable;
    private boolean mActionDrawableVisible;
    private OnActionDrawableClickListener mOnActionDrawableClickListener;
    private int mActionId = NORMAL_ACTION_CLEAR;
    private OnActionDrawableVisibleChangeListener mActionDrawableVisibleChangeListener;
    private PhoneNumberFormatWatcher mPhoneNumberFormatWatcher;
    private int hintSize;

    private double minNumber = Double.NaN;
    private double increaseNumber = Double.NaN;
    private int mDecimalLimit;

    private OnInputNumberValidatedListener mOnInputNumberValidatedListener;


    public ExEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ExEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExEditText);
            setFormatMode(a.getInteger(R.styleable.ExEditText_formatMode, FormatMode.NORMAL));
            hintSize = a.getDimensionPixelSize(R.styleable.ExEditText_hintSize, hintSize);
            if (hintSize != 0) {
                setSizeableHint(getHint());
            }
            mDecimalLimit = a.getInt(R.styleable.ExEditText_decimal_limit, mDecimalLimit);
            a.recycle();
        }
        mActionDrawable = getResources().getDrawable(R.drawable.ic_clear_text_circle);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setActionVisible(hasFocus() && getText().length() > 0);
                if ((!Double.isNaN(minNumber) || !Double.isNaN(increaseNumber)) && mOnInputNumberValidatedListener != null) {
                    checkNumberValidated(minNumber, increaseNumber, getText().toString(), mOnInputNumberValidatedListener);
                }
            }
        });

        if (mDecimalLimit != 0) {
            InputFilter[] filter = getFilters();
            if (filter == null) {
                filter = new InputFilter[]{new DecimalDigitsInputFilter(mDecimalLimit)};
            } else {
                int length = filter.length;
                filter = Arrays.copyOf(filter, length + 1);
                filter[length] = new DecimalDigitsInputFilter(mDecimalLimit);
            }
            setFilters(filter);
        }
        if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            disableCopyAndPaste();
        }
    }

    private void disableCopyAndPaste() {
        setLongClickable(false);
        setTextIsSelectable(false);
        ActionMode.Callback cb = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setCustomInsertionActionModeCallback(cb);
        } else {
            setCustomSelectionActionModeCallback(cb);
        }
    }

    public void checkNumberValidated() {
        if ((!Double.isNaN(minNumber) || !Double.isNaN(increaseNumber)) && mOnInputNumberValidatedListener != null) {
            checkNumberValidated(minNumber, increaseNumber, getText().toString(), mOnInputNumberValidatedListener);
        }
    }

    private void checkNumberValidated(double minNumber, double increaseNumber, String curInput, OnInputNumberValidatedListener mOnInputNumberValidatedListener) {
        int errorTag = ErrorTag.VALIDATE;
        //检查是否为空
        if (TextUtils.isEmpty(curInput)) {
            errorTag = ErrorTag.INPUT_EMPTY;
            mOnInputNumberValidatedListener.onValidated(minNumber, increaseNumber, -1, false, errorTag);
            return;
        }

        double input = StringUtil.toDouble(curInput);
        //最低数值检查
        if (!DecimalMathUtil.compareWithEqual(curInput, String.valueOf(minNumber))) {
            errorTag = ErrorTag.NOT_ENOUTH;
            mOnInputNumberValidatedListener.onValidated(minNumber, increaseNumber, input, false, errorTag);
            return;
        }

        //递增数值检查
        boolean validated = true;
        String remainder = "0";
        if (increaseNumber > 0) {
            remainder = DecimalMathUtil.remainder(Double.toString(input - minNumber), Double.toString(increaseNumber), 2);
        }
        validated = validated && decimalCompaire(remainder, "0.0") == 0;
        if (!validated) {
            errorTag = ErrorTag.INVALIDATED_INCREASE;
        }
        mOnInputNumberValidatedListener.onValidated(minNumber, increaseNumber, input, validated, errorTag);
    }

    private int decimalCompaire(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.compareTo(b2);
    }

    public ExEditText setHintSize(int hintSizeSp) {
        this.hintSize = UIHelper.sp2px(hintSizeSp);
        return this;
    }

    public ExEditText setSizeableHint(CharSequence hint) {
        return setSizeableHint(hint, 0);
    }

    public ExEditText setSizeableHint(CharSequence hint, int sizesp) {
        CharSequence hintResult;
        if (sizesp != 0) {
            setHintSize(sizesp);
        }
        if (hintSize == 0 || TextUtils.isEmpty(hint)) {
            hintResult = hint;
        } else {
            hintResult = new SpannableString(hint);
            AbsoluteSizeSpan span = new AbsoluteSizeSpan(hintSize, false);
            ((SpannableString) hintResult).setSpan(span, 0, hintResult.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        super.setHint(hintResult);
        return this;
    }

    public ExEditText setFormatMode(@FormatMode int mode) {
        formateMode = mode;
        if (formateMode < 0) {
            formateMode = FormatMode.NORMAL;
        }
        return applyFormatMode();
    }

    private ExEditText applyFormatMode() {
        switch (formateMode) {
            case FormatMode.PHONE:
                if (mPhoneNumberFormatWatcher == null) {
                    mPhoneNumberFormatWatcher = new PhoneNumberFormatWatcher();
                }
                addTextChangedListener(mPhoneNumberFormatWatcher);
                break;
            case FormatMode.NORMAL:
            default:
                if (mPhoneNumberFormatWatcher != null) {
                    removeTextChangedListener(mPhoneNumberFormatWatcher);
                }
                break;
        }
        return this;
    }

    public String getNonFormatText() {
        String result = getText().toString();
        switch (formateMode) {
            case FormatMode.PHONE:
                if (!TextUtils.isEmpty(result)) {
                    return result.trim().replace(" ", "");
                }
                return result;
            case FormatMode.NORMAL:
            default:
                return result;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mActionDrawable != null && event.getX() <= (getWidth() - getPaddingRight())
                        && event.getX() >= (getWidth() - getPaddingRight() - mActionDrawable.getBounds().width())) {
                    if (mOnActionDrawableClickListener == null || !mOnActionDrawableClickListener.onActionDrawableClick(mActionId)) {
                        if (mActionId == NORMAL_ACTION_CLEAR) {
                            clearText();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public ExEditText setActionDrawable(@DrawableRes int drawableId, int actionId) {
        return setActionDrawalbe(getResources().getDrawable(drawableId), actionId);
    }

    public ExEditText setActionDrawalbe(Drawable drawable, int actionId) {
        mActionDrawable = drawable;
        mActionId = actionId;
        return this;
    }

    public OnActionDrawableClickListener getOnActionDrawableClickListener() {
        return mOnActionDrawableClickListener;
    }

    public ExEditText setOnActionDrawableClickListener(OnActionDrawableClickListener onActionDrawableClickListener) {
        mOnActionDrawableClickListener = onActionDrawableClickListener;
        return this;
    }

    public void setActionDrawableGravity(int gravity) {
        mActionDrawableGravity = gravity;
        setActionVisible(mActionDrawableVisible);
    }

    public void setActionVisible(boolean visible) {
        if (!isEnabled()) return;
        Drawable left = null;
        Drawable top = null;
        Drawable right = null;
        Drawable bottom = null;
        switch (mActionDrawableGravity) {
            case Gravity.LEFT:
            case Gravity.START:
                left = visible ? mActionDrawable : null;
                break;
            case Gravity.TOP:
                top = visible ? mActionDrawable : null;
                break;
            case Gravity.RIGHT:
            case Gravity.END:
                right = visible ? mActionDrawable : null;
                break;
            case Gravity.BOTTOM:
                bottom = visible ? mActionDrawable : null;
                break;
        }
        mActionDrawableVisible = visible;
        setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        if (mActionDrawableVisibleChangeListener != null) {
            mActionDrawableVisibleChangeListener.onVisibleChange(visible);
        }
    }

    public OnActionDrawableVisibleChangeListener getActionDrawableVisibleChangeListener() {
        return mActionDrawableVisibleChangeListener;
    }

    public ExEditText setOnActionDrawableVisibleChangeListener(OnActionDrawableVisibleChangeListener actionDrawableVisibleChangeListener) {
        mActionDrawableVisibleChangeListener = actionDrawableVisibleChangeListener;
        return this;
    }

    public interface OnActionDrawableClickListener {
        /**
         * 返回ture，则父类不处理
         *
         * @param actionid
         * @return
         */
        boolean onActionDrawableClick(int actionid);
    }

    public void clearText() {
        getText().clear();
    }

    public interface OnActionDrawableVisibleChangeListener {
        void onVisibleChange(boolean visible);
    }

    //-----------------------------------------textwatcher-----------------------------------------
    private class PhoneNumberFormatWatcher implements TextWatcher {
        // 特殊下标位置
        private static final int PHONE_INDEX_3 = 3;
        private static final int PHONE_INDEX_4 = 4;
        private static final int PHONE_INDEX_8 = 8;
        private static final int PHONE_INDEX_9 = 9;

        private StringBuilder sb = new StringBuilder();

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s == null || s.length() == 0) {
                return;
            }
            try {
                sb.replace(0, sb.length(), "");
                for (int i = 0; i < s.length(); i++) {
                    if (i != PHONE_INDEX_3 && i != PHONE_INDEX_8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == PHONE_INDEX_4 || sb.length() == PHONE_INDEX_9) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }

                    setText(sb.toString());
                    setSelection(index);
                }
            } catch (Exception e) {
                e.printStackTrace();
                setText(sb.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public OnInputNumberValidatedListener getOnInputNumberValidatedListener() {
        return mOnInputNumberValidatedListener;
    }

    public ExEditText setOnInputNumberValidatedListener(OnInputNumberValidatedListener onInputNumberValidatedListener) {
        mOnInputNumberValidatedListener = onInputNumberValidatedListener;
        return this;
    }

    public double getMinNumber() {
        return minNumber;
    }

    public ExEditText setMinNumber(double minNumber) {
        this.minNumber = minNumber;
        return this;
    }

    public double getIncreaseNumber() {
        return increaseNumber;
    }

    public ExEditText setIncreaseNumber(double increaseNumber) {
        this.increaseNumber = increaseNumber;
        return this;
    }

    public interface OnInputNumberValidatedListener {
        void onValidated(double minValue, double increaseValue, double curValue, boolean validated, @ErrorTag int tag);
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        private final int decimalDigits;

        /**
         * Constructor.
         *
         * @param decimalDigits maximum decimal digits
         */
        public DecimalDigitsInputFilter(int decimalDigits) {
            this.decimalDigits = decimalDigits;
        }

        @Override
        public CharSequence filter(CharSequence source,
                                   int start,
                                   int end,
                                   Spanned dest,
                                   int dstart,
                                   int dend) {


            int dotPos = -1;
            int len = dest.length();
            for (int i = 0; i < len; i++) {
                char c = dest.charAt(i);
                if (c == '.' || c == ',') {
                    dotPos = i;
                    break;
                }
            }
            if (dotPos >= 0) {

                // protects against many dots
                if (source.equals(".") || source.equals(",")) {
                    return "";
                }
                // if the text is entered before the dot
                if (dend <= dotPos) {
                    return null;
                }
                if (len - dotPos > decimalDigits) {
                    return "";
                }
            }

            return null;
        }

    }
}
