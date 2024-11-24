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

import com.anychart.core.ui.Label;
import com.example.myapplication.R;

/**
 * TODO: document your custom view class.
 */
public class SummedView extends ConstraintLayout {

    public SummedView(Context context) {
        super(context);
        init(null, 0);
    }

    public SummedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SummedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TextView titleTextView = new TextView(getContext());
        titleTextView.setText("Distance Totals");
        titleTextView.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
        titleTextView.setId(View.generateViewId());
        titleTextView.setTextSize(14);
        titleTextView.setTextColor(Color.BLACK);
        ConstraintLayout.LayoutParams titleTextViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        titleTextViewParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        titleTextViewParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
//        titleTextViewParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        titleTextViewParams.topMargin = 20;
        titleTextView.setLayoutParams(titleTextViewParams);
        addView(titleTextView);


        GridView distancGridView = new GridView(getContext());
//        distancGridView.setBackgroundColor(Color.RED);
        distancGridView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams distanceGridViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        distanceGridViewParams.topToBottom = titleTextView.getId();
        distanceGridViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
//        distanceGridViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
//        distanceGridViewParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        distanceGridViewParams.topMargin = 20;
        distanceGridViewParams.matchConstraintPercentWidth = 0.5f;
        distancGridView.setLayoutParams(distanceGridViewParams);
        addView(distancGridView);

        GridView avgDailyGridView = new GridView(getContext());
//        avgDistancGridView.setBackgroundColor(Color.BLUE);
        avgDailyGridView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams avgDailyGridViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        avgDailyGridViewParams.topToBottom = titleTextView.getId();
        avgDailyGridViewParams.startToEnd = distancGridView.getId();
        avgDailyGridViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        avgDailyGridViewParams.bottomToBottom = distancGridView.getId();
        avgDailyGridViewParams.topMargin = 20;
        avgDailyGridViewParams.matchConstraintPercentWidth = 0.5f;
        avgDailyGridView.setLayoutParams(avgDailyGridViewParams);
        addView(avgDailyGridView);

        GridView avgWeekGridView = new GridView(getContext());
//        avgWeekGridView.setBackgroundColor(Color.BLUE);
        avgWeekGridView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams avgWeekGridViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        avgWeekGridViewParams.topToBottom = distancGridView.getId();
        avgWeekGridViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
//        avgWeekGridViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        avgWeekGridViewParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        avgWeekGridViewParams.topMargin = 20;
        avgWeekGridViewParams.matchConstraintPercentWidth = 0.5f;
        avgWeekGridView.setLayoutParams(avgWeekGridViewParams);
        addView(avgWeekGridView);


        View topLine = new View(getContext());
        topLine.setId(View.generateViewId());
        topLine.setBackgroundColor(Color.BLACK); // Line color
        ConstraintLayout.LayoutParams topLineParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, // Width of the line
                1 // Height of the line (for horizontal line)
        );
        topLineParams.topToTop = distancGridView.getId();
        topLine.setLayoutParams(topLineParams);
        addView(topLine);

        View middleLine = new View(getContext());
        middleLine.setId(View.generateViewId());
        middleLine.setBackgroundColor(Color.BLACK); // Line color
        ConstraintLayout.LayoutParams middleLineParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, // Width of the line
                1 // Height of the line (for horizontal line)
        );
        middleLineParams.topToBottom = distancGridView.getId();
        middleLine.setLayoutParams(middleLineParams);
        addView(middleLine);


        View bottomLine = new View(getContext());
        bottomLine.setId(View.generateViewId());
        bottomLine.setBackgroundColor(Color.BLACK); // Line color
        ConstraintLayout.LayoutParams bottomLineParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, // Width of the line
                1 // Height of the line (for horizontal line)
        );
        bottomLineParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
//        bottomLineParams.bottomMargin = 2;
        bottomLine.setLayoutParams(bottomLineParams);
        addView(bottomLine);

        View verticalLine = new View(getContext());
        verticalLine.setId(View.generateViewId());
        verticalLine.setBackgroundColor(Color.BLACK); // Line color
        ConstraintLayout.LayoutParams verticalLineParams = new ConstraintLayout.LayoutParams(
                1, // Width of the line
                0 // Height of the line (for horizontal line)
        );
        verticalLineParams.topToTop = distancGridView.getId();
        verticalLineParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        verticalLineParams.startToEnd = distancGridView.getId();
        verticalLine.setLayoutParams(verticalLineParams);
        addView(verticalLine);


//        View rightLine = new View(getContext());
//        rightLine.setId(View.generateViewId());
//        rightLine.setBackgroundColor(Color.BLACK); // Line color
//        ConstraintLayout.LayoutParams rightLineParams = new ConstraintLayout.LayoutParams(
//                1, // Width of the line
//                0 // Height of the line (for horizontal line)
//        );
//        rightLineParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
//        rightLineParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
//        rightLineParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
//        rightLine.setLayoutParams(rightLineParams);
//        addView(rightLine);



    }

}

