package com.example.myapplication.ui.record;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.myapplication.R;
import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.RunningRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO: document your custom view class.
 */

public class StatisticView extends ConstraintLayout {

    public enum StatisticType {
        WEEK,
        MONTH,
        YEAR
    }

    public interface StatisticViewListener {
        void onStatisticTypeChanged(StatisticType type);
    }

    private StatisticViewListener listener;
    private AnyChartView anyChartView;
    private Cartesian cartesian;
    private int height = 0;
    private final int segmentControlHeight = 150;

    public StatisticView(Context context, int height) {
        super(context);
        this.height = height;
        init(context);
    }

    public StatisticView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StatisticView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context){
//        this.setBackgroundColor(Color.GREEN);
        // Set ConstraintLayout as the root layout
        ConstraintLayout rootConstraintLayout = new ConstraintLayout(context);
        LayoutParams rootParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        rootConstraintLayout.setLayoutParams(rootParams);
        // Add the ConstraintLayout to the root of the custom view
        this.addView(rootConstraintLayout);

        // Add the segment control
        LinearLayout segmentControlLayout = new LinearLayout(getContext());
        segmentControlLayout.setId(View.generateViewId());
        segmentControlLayout.setOrientation(LinearLayout.VERTICAL);
        ConstraintLayout.LayoutParams segmentControlParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                segmentControlHeight
        );
        segmentControlParams.topToTop = LayoutParams.PARENT_ID;
        segmentControlParams.startToStart = LayoutParams.PARENT_ID;
        segmentControlParams.endToEnd = LayoutParams.PARENT_ID;
        segmentControlLayout.setLayoutParams(segmentControlParams);

        // Set the root layout as the content view
        rootConstraintLayout.addView(segmentControlLayout);

        // Create the segment control
        createSegmentControl(getContext(), segmentControlLayout);

        // Add the graph layout
        ConstraintLayout graphLayout = new ConstraintLayout(getContext());
        graphLayout.setId(View.generateViewId());
//        graphLayout.setBackgroundColor(Color.BLUE);
        View parentView = findViewById(segmentControlLayout.getId());
        int parentHeight = parentView.getHeight(); // Height in pixels

        ConstraintLayout.LayoutParams graphLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
//        graphLayoutParams.height = 100;
        graphLayoutParams.topToBottom = segmentControlLayout.getId();
        graphLayoutParams.startToStart = LayoutParams.PARENT_ID;
        graphLayoutParams.endToEnd = LayoutParams.PARENT_ID;
        graphLayout.setLayoutParams(graphLayoutParams);
        rootConstraintLayout.addView(graphLayout);

        createGraph(getContext(), graphLayout);


    }

    public void setStatisticViewListener(StatisticViewListener listener) {
        this.listener = listener;
    }

    // Create a method to generate a Segment Control
    private void createSegmentControl(Context context, LinearLayout parentLayout) {
        // Create a horizontal LinearLayout for the segment control
        LinearLayout segmentControl = new LinearLayout(context);
        segmentControl.setOrientation(LinearLayout.HORIZONTAL);
        segmentControl.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        segmentControl.setGravity(Gravity.CENTER);
//        segmentControl.setBackgroundColor(Color.RED);

        // Define selected and unselected text styles
        int selectedColor = Color.BLUE; // Selected text color
        int unselectedColor = Color.GRAY; // Unselected text color
        int underlineHeight = (int) (2 * context.getResources().getDisplayMetrics().density); // 2dp underline

        // Text labels for each segment
        String[] segments = {"Week", "Month", "Year"};
        int selectedIndex = 0; // Default selected index (Month)

        // Loop to create segment TextViews
        for (int i = 0; i < segments.length; i++) {
            TextView segmentTextView = new TextView(context);
            segmentTextView.setText(segments[i]);
            segmentTextView.setGravity(Gravity.CENTER);
            segmentTextView.setTextSize(16);
            segmentTextView.setPadding(16, 16, 16, 16);
            segmentTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f // Equal weight for all segments
            ));

            // Set initial styles (selected or unselected)
            if (i == selectedIndex) {
                segmentTextView.setTextColor(selectedColor);
                segmentTextView.setPaintFlags(segmentTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else {
                segmentTextView.setTextColor(unselectedColor);
                segmentTextView.setPaintFlags(segmentTextView.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
            }

            // Handle click events to change selection
            int finalIndex = i; // Capture index for listener
            segmentTextView.setOnClickListener(v -> {
                // Update all TextView styles on click
                for (int j = 0; j < segmentControl.getChildCount(); j++) {
                    TextView child = (TextView) segmentControl.getChildAt(j);
                    if (j == finalIndex) {
                        child.setTextColor(selectedColor);
                        child.setPaintFlags(child.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    } else {
                        child.setTextColor(unselectedColor);
                        child.setPaintFlags(child.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                    }
                }

                // Perform your action based on the selected segment
                // TODO switch (finalIndex) { ... }
                Toast.makeText(context, segments[finalIndex] + " Selected", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    switch (finalIndex) {
                        case 0:
                            listener.onStatisticTypeChanged(StatisticType.WEEK);
                            break;
                        case 1:
                            listener.onStatisticTypeChanged(StatisticType.MONTH);
                            break;
                        case 2:
                            listener.onStatisticTypeChanged(StatisticType.YEAR);
                            break;
                    }
                }

            });

            // Add the TextView to the segment control
            segmentControl.addView(segmentTextView);
        }

        // Add the segment control to the parent layout
        parentLayout.addView(segmentControl);
    }



    private void createGraph(Context context, ConstraintLayout parentLayout) {

        anyChartView = new AnyChartView(context);
        anyChartView.setId(View.generateViewId());
//        anyChartView.setBackgroundColor(Color.BLUE);
        ConstraintLayout.LayoutParams anyChartViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                height - segmentControlHeight
        );
        anyChartViewParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        anyChartViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        anyChartViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        anyChartView.setLayoutParams(anyChartViewParams);
        parentLayout.addView(anyChartView);

        cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian);

    }


    public Cartesian createColumnChart(){
        //***** Read data from SQLiteDatabase *********/
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysAgo = calendar.getTime();
        String nowS =  now.toString();

        List<Map<String,String>> last7Days = getLastWeekDates();

        //***** Create column chart using AnyChart library *********/
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        for (Map<String,String> day: last7Days) {
            System.out.println(day.get("date") + " " + day.get("steps"));
            data.add(new ValueDataEntry(day.get("date"), Integer.valueOf(day.get("steps"))));
        }

        Column column = cartesian.column(data);

        //***** Modify the UI of the chart *********/
        column.fill("#1EB980");
        column.stroke("#1EB980");

        column.tooltip()
                .titleFormat("At day: {%X}")
                .format("{%Value} Steps")
                .anchor(Anchor.RIGHT_BOTTOM);

        column.tooltip()
                .position(Position.RIGHT_TOP)
                .offsetX(0d)
                .offsetY(5);

        // Modifying properties of cartesian
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yScale().minimum(0);


        cartesian.yAxis(0).title("Number of steps");
        cartesian.xAxis(0).title("Date");
        cartesian.background().fill("#00000000");
        cartesian.animation(true);

        return cartesian;
    }

    public void refreshGraph(List<RunningRecord> records, List<Map<String,String>> recordsList) {
        cartesian.removeAllSeries();

        Map<String, RunningRecord> recordsMap = new LinkedHashMap<>();
        for ( RunningRecord record: records) {
            recordsMap.put(record.getTimestamp(), record);
        }
        for ( Map<String,String> day: recordsList) {
            RunningRecord record = recordsMap.get(day.get("date"));
            if (record != null) {
                day.put("steps", record.getDistance());
            }
        }


        List<DataEntry> data = new ArrayList<>();
        for (Map<String,String> day: recordsList) {
            System.out.println(day.get("date") + " " + day.get("steps"));
            data.add(new ValueDataEntry(day.get("date"), Integer.valueOf(day.get("steps"))));
        }

        cartesian.column(data);
        //        anyChartView.invalidate();
    }

    public List<Map<String,String>> getLastWeekDates() {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        // Store the dates in a list
        List<Map<String,String>> lastWeekDates = new ArrayList<>();

        // Get the dates for the last 7 days
        for (int i = 0; i < 7; i++) {
            // Format and add the current date to the list
            String formattedDate = dateFormat.format(calendar.getTime());
            Map<String,String> dateMap = new HashMap<>();
            dateMap.put("date", formattedDate);
            dateMap.put("steps", "0");
            lastWeekDates.add(dateMap);

            // Move the calendar back by one day
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        Collections.reverse(lastWeekDates);
        return lastWeekDates;
    }


}