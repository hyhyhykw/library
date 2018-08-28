package com.hy.picker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.hy.library.R;
import com.hy.picker.core.IMGText;
import com.hy.picker.view.IMGColorGroup;

/**
 * Created by felix on 2017/12/1 上午11:21.
 */

public class IMGTextEditDialog extends Dialog implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    private EditText mEditText;

    private Callback mCallback;

    private IMGText mDefaultText;

    private IMGColorGroup mColorGroup;

    public IMGTextEditDialog(Context context, Callback callback) {
        super(context, R.style.PickerTextDialog);
        setContentView(R.layout.picker_text_dialog);
        mCallback = callback;
        Window window = getWindow();
        if (window != null) {
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mColorGroup = findViewById(R.id.picker_cg_colors);
        mColorGroup.setOnCheckedChangeListener(this);
        mEditText = findViewById(R.id.picker_et_text);

        findViewById(R.id.picker_tv_cancel).setOnClickListener(this);
        findViewById(R.id.picker_tv_done).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mDefaultText != null) {
            mEditText.setText(mDefaultText.getText());
            mEditText.setTextColor(mDefaultText.getColor());
            if (!mDefaultText.isEmpty()) {
                mEditText.setSelection(mEditText.length());
            }
            mDefaultText = null;
        } else mEditText.setText("");
        mColorGroup.setCheckColor(mEditText.getCurrentTextColor());
    }

    public void setText(IMGText text) {
        mDefaultText = text;
    }

    public void reset() {
        setText(new IMGText(null, Color.WHITE));
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.picker_tv_done) {
            onDone();
        } else if (vid == R.id.picker_tv_cancel) {
            dismiss();
        }
    }

    private void onDone() {
        String text = mEditText.getText().toString();
        if (!TextUtils.isEmpty(text) && mCallback != null) {
            mCallback.onText(new IMGText(text, mEditText.getCurrentTextColor()));
        }
        dismiss();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mEditText.setTextColor(mColorGroup.getCheckColor());
    }

    public interface Callback {

        void onText(IMGText text);
    }
}
