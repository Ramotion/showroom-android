package com.ramotion.showroom.examples.fluidslider;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ramotion.fluidslider.FluidSlider;
import com.ramotion.showroom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Unit;

public class FluidSliderActivity extends AppCompatActivity {

    @BindView(R.id.textView) TextView mTextView;
    @BindView(R.id.fluidSlider) FluidSlider mSlider;

    private final int max = 45;
    private final int min = 10;
    private final int total = max - min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fs_activity_main);
        ButterKnife.bind(this);

        mSlider.setBeginTrackingListener(() -> {
            mTextView.setVisibility(View.INVISIBLE);
            return Unit.INSTANCE;
        });

        mSlider.setEndTrackingListener(() -> {
            mTextView.setVisibility(View.VISIBLE);
            return Unit.INSTANCE;
        });

        mSlider.setPositionListener(pos -> {
            String value = String.valueOf( (int)(min + total * pos) );
            mSlider.setBubbleText(value);
            return Unit.INSTANCE;
        });

        mSlider.setPosition(0.3f);
        mSlider.setStartText(String.valueOf(min));
        mSlider.setEndText(String.valueOf(max));
    }

}