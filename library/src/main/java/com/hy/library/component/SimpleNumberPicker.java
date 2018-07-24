package com.hy.library.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hy.library.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created time : 2018/5/24 16:44.
 *
 * @author HY
 */
public class SimpleNumberPicker extends RelativeLayout {

    @BindView(R.id.iv_minus)
    ImageView mIvMinus;
    @BindView(R.id.tv_number)
    TextView mTvNumber;
    @BindView(R.id.iv_plus)
    ImageView mIvPlus;

    public SimpleNumberPicker(Context context) {
        this(context, null);
    }

    public SimpleNumberPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.widget_number_pick, this, true);
        ButterKnife.bind(this);
        mIvMinus.setEnabled(false);
    }

    private int min = 1;
    private int number = 1;

    public void setNumber(int number) {
        if (number < min) number = min;
        this.number = number;
        init();
    }

    public void minus() {
        setNumber(getNumber() - 1);
    }

    public void plus() {
        setNumber(getNumber() + 1);
    }

    private void init() {
        mTvNumber.setText(String.valueOf(number));
        mIvMinus.setEnabled(number != min);
    }

    public int getNumber() {
        return number;
    }

    @OnClick({R.id.iv_minus, R.id.iv_plus})
    public void onClick(View view) {
//        int number = getNumber();
        switch (view.getId()) {
            case R.id.iv_minus:
//                setNumber(number - 1);
                if (null != mNumberListener)
                    mNumberListener.onMinus();
                break;
            case R.id.iv_plus:
//                setNumber(number + 1);
                if (null != mNumberListener)
                    mNumberListener.onPlus();
                break;
        }
    }

    public void disable() {
        mIvMinus.setEnabled(false);
        mIvPlus.setEnabled(false);
    }

    public void enable() {
        mIvMinus.setEnabled(true);
        mIvPlus.setEnabled(true);
    }

    public interface NumberListener {
        void onPlus();

        void onMinus();
    }

    private NumberListener mNumberListener;

    public void setNumberListener(NumberListener numberListener) {
        mNumberListener = numberListener;
    }
}
