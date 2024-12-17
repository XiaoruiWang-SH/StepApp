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

import java.util.List;
import java.util.Map;

/**
 * TODO: document your custom view class.
 */
public class SummedView extends ConstraintLayout {
    public enum TYPE {
        OVERALL,
        DAILY,
    }

    private TYPE type;
    GridView firstGridView;
    GridView secondGridView;
    GridView thirdGridView;
    GridView fourthGridView;

    public SummedView(Context context, TYPE type) {
        super(context);
        this.type = type;
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


        firstGridView = new GridView(getContext());
//        distancGridView.setBackgroundColor(Color.RED);
        firstGridView.setId(View.generateViewId());
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
        firstGridView.setLayoutParams(distanceGridViewParams);
        addView(firstGridView);

        secondGridView = new GridView(getContext());
//        avgDistancGridView.setBackgroundColor(Color.BLUE);
        secondGridView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams avgDailyGridViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        avgDailyGridViewParams.topToBottom = titleTextView.getId();
        avgDailyGridViewParams.startToEnd = firstGridView.getId();
        avgDailyGridViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        avgDailyGridViewParams.bottomToBottom = firstGridView.getId();
        avgDailyGridViewParams.topMargin = 20;
        avgDailyGridViewParams.matchConstraintPercentWidth = 0.5f;
        secondGridView.setLayoutParams(avgDailyGridViewParams);
        addView(secondGridView);

        thirdGridView = new GridView(getContext());
//        avgWeekGridView.setBackgroundColor(Color.BLUE);
        thirdGridView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams avgWeekGridViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        avgWeekGridViewParams.topToBottom = firstGridView.getId();
        avgWeekGridViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
//        avgWeekGridViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        avgWeekGridViewParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        avgWeekGridViewParams.topMargin = 20;
        avgWeekGridViewParams.matchConstraintPercentWidth = 0.5f;
        thirdGridView.setLayoutParams(avgWeekGridViewParams);
        addView(thirdGridView);


        View topLine = new View(getContext());
        topLine.setId(View.generateViewId());
        topLine.setBackgroundColor(Color.BLACK); // Line color
        ConstraintLayout.LayoutParams topLineParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, // Width of the line
                1 // Height of the line (for horizontal line)
        );
        topLineParams.topToTop = firstGridView.getId();
        topLine.setLayoutParams(topLineParams);
        addView(topLine);

        View middleLine = new View(getContext());
        middleLine.setId(View.generateViewId());
        middleLine.setBackgroundColor(Color.BLACK); // Line color
        ConstraintLayout.LayoutParams middleLineParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, // Width of the line
                1 // Height of the line (for horizontal line)
        );
        middleLineParams.topToBottom = firstGridView.getId();
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
        verticalLineParams.topToTop = firstGridView.getId();
        verticalLineParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        verticalLineParams.startToEnd = firstGridView.getId();
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


    public void configureView(TYPE type, List<Map<String, String>> titleList) {

        Map<String, String> firstMap = titleList.get(0);
        firstGridView.configureView(firstMap.get("numberTextViewText"), firstMap.get("unitTextViewText"), firstMap.get("titleTextViewText"));

        Map<String, String> secondMap = titleList.get(1);
        secondGridView.configureView(secondMap.get("numberTextViewText"), secondMap.get("unitTextViewText"), secondMap.get("titleTextViewText"));

        Map<String, String> thirdMap = titleList.get(2);
        thirdGridView.configureView(thirdMap.get("numberTextViewText"), thirdMap.get("unitTextViewText"), thirdMap.get("titleTextViewText"));


    }

}

