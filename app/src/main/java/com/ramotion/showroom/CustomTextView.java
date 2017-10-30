package com.ramotion.showroom;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {


    public CustomTextView(Context context) {
        super(context);
    }


    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.getLineHeight();

        this.getLineCount();

        this.getHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int height = getMeasuredHeight();

        setRestrictions(height);
        this.setMeasuredDimension(parentWidth, height);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        setRestrictions(h);
    }

    private void setRestrictions(int height) {
        int maxLines = height / getLineHeight();
        if (maxLines == 0) {
            setTextSize(height);
        }
        this.setMaxLines(maxLines > 1 ? maxLines : 1);
        this.setEllipsize(TextUtils.TruncateAt.END);
    }


}
