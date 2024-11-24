package com.example.myapplication.ui.record;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;

/**
 * TODO: document your custom view class.
 */
public class GridView extends ConstraintLayout {

    public GridView(Context context) {
        super(context);
        init(null, 0);
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TextView numberTextView = new TextView(getContext());
        numberTextView.setText("0");
        numberTextView.setId(View.generateViewId());
        numberTextView.setTextSize(30);
        numberTextView.setTextColor(Color.BLACK);
        ConstraintLayout.LayoutParams numberTextViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        numberTextViewParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        numberTextViewParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        numberTextViewParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        numberTextViewParams.horizontalBias = 0.5f;
        numberTextView.setLayoutParams(numberTextViewParams);
        addView(numberTextView);


        TextView unitTextView = new TextView(getContext());
        unitTextView.setText("Steps");
        unitTextView.setId(View.generateViewId());
        unitTextView.setTextSize(12);
        unitTextView.setTextColor(Color.BLACK);
        ConstraintLayout.LayoutParams unitTextViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        unitTextViewParams.bottomToBottom = numberTextView.getId();
        unitTextViewParams.startToEnd = numberTextView.getId();
        unitTextViewParams.setMarginStart(10);
        unitTextView.setLayoutParams(unitTextViewParams);
        addView(unitTextView);


        TextView titleTextView = new TextView(getContext());
        titleTextView.setText("Total Distance");
        titleTextView.setId(View.generateViewId());
        titleTextView.setTextSize(14);
        titleTextView.setTextColor(Color.BLACK);
        ConstraintLayout.LayoutParams titleTextViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        titleTextViewParams.topToBottom = numberTextView.getId();
        titleTextViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        titleTextViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        titleTextViewParams.topMargin = 20;
        titleTextViewParams.bottomMargin = 20;
        titleTextViewParams.horizontalBias = 0.5f;
        titleTextView.setLayoutParams(titleTextViewParams);
        addView(titleTextView);

    }

}