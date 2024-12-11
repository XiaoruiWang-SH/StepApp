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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TODO: document your custom view class.
 */
public class StatisticView extends ConstraintLayout {

    AnyChartView anyChartView;
    private int height = 0;
    private final int segmentControlHeight = 150;


    Map<String, Double> daysSteps = new LinkedHashMap<>();
    private Cartesian cartesian;
    private List<DataEntry> dataEntries;


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

                List<Map<String, String>> entries = List.of(
                        Map.of("date", "2024-10-26", "steps", "110"),
                        Map.of("date", "2024-10-22", "steps", "118"),
                        Map.of("date", "2024-10-24", "steps", "115"),
                        Map.of("date", "2024-10-23", "steps", "120"),
                        Map.of("date", "2024-10-25", "steps", "15"),
                        Map.of("date", "2024-10-21", "steps", "17")
                );
                this.configureChart(entries);

                // Perform your action based on the selected segment
                Toast.makeText(context, segments[finalIndex] + " Selected", Toast.LENGTH_SHORT).show();
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

        this.cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(this.cartesian);

    }


    public Cartesian createColumnChart(){

        dataEntries = new ArrayList<>();
        List<Map<String, String>> entries = List.of(
                Map.of("date", "2024-10-26", "steps", "10"),
                Map.of("date", "2024-10-22", "steps", "18"),
                Map.of("date", "2024-10-24", "steps", "15"),
                Map.of("date", "2024-10-23", "steps", "20"),
                Map.of("date", "2024-10-25", "steps", "5"),
                Map.of("date", "2024-10-21", "steps", "7"),
                Map.of("date", "2024-10-20", "steps", "24")
        );

        for (Map<String,String> entry : entries) {
            dataEntries.add(new ValueDataEntry(entry.get("date"),
                    Integer.parseInt(Objects.requireNonNull(entry.get("steps")))));
        }

//
//        //***** Read data from SQLiteDatabase *********/
//        Date now = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(now);
//        calendar.add(Calendar.DAY_OF_YEAR, -7);
//        Date sevenDaysAgo = calendar.getTime();
//        String nowS =  now.toString();
//
//        List<String> last7Days = getLastWeekDates();
//
////        Map<String, Integer> last7DaysSteps = StepAppOpenHelper.loadStepsByLast7Days(getContext(), last7Days);
//        // test data
//        Map<String, Integer> last7DaysSteps = Map.of(
//                "2024-10-26", 10,
//                "2024-10-22", 18,
//                "2024-10-24", 15,
//                "2024-10-23", 20,
//                "2024-10-25", 5,
//                "2024-10-21", 7,
//                "2024-10-20", 24);
//
//        LinkedHashMap<String, Integer> linkedHashMap = last7DaysSteps.entrySet()
//                .stream()
//                .sorted(Comparator.comparing(entry -> {
//                    String[] parts = entry.getKey().split("-");
//                    int year = Integer.parseInt(parts[0]);
//                    int month = Integer.parseInt(parts[1]);
//                    int day = Integer.parseInt(parts[2]);
//                    return new int[]{year, month, day}; // Use array for multiple comparisons
//                }, Comparator.comparingInt((int[] arr) -> arr[0]) // Compare year
//                        .thenComparingInt(arr -> arr[1])                // Then compare month
//                        .thenComparingInt(arr -> arr[2]))).collect(Collectors.toMap(
//                        entry -> entry.getKey(),   // Corrected with lambda expressions
//                        entry -> entry.getValue(), // Corrected with lambda expressions
//                        (existing, replacement) -> existing,  // Handle key conflicts
//                        LinkedHashMap::new         // Use LinkedHashMap to maintain order
//                ));              // Then compare day
//
//        Map<String, Integer> graph_map = new LinkedHashMap<>();
//
//        for (String d: last7Days) {
//            Integer v = linkedHashMap.get(d);
//            if (v == null) v = 0;
//            graph_map.put(d,v);
//        }
//
//        //***** Create column chart using AnyChart library *********/
        cartesian = AnyChart.column();
//
//        List<DataEntry> data = new ArrayList<>();
//
//        for (Map.Entry<String,Integer> entry : graph_map.entrySet())
//            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));

        Column column = cartesian.column(this.dataEntries);

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

    public List<String> getLastWeekDates() {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        // Store the dates in a list
        List<String> lastWeekDates = new ArrayList<>();

        // Get the dates for the last 7 days
        for (int i = 0; i < 7; i++) {
            // Format and add the current date to the list
            String formattedDate = dateFormat.format(calendar.getTime());
            lastWeekDates.add(formattedDate);

            // Move the calendar back by one day
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        Collections.reverse(lastWeekDates);
        return lastWeekDates;
    }

    public void configureChart(List<Map<String, String>> entries) {
        dataEntries.clear();
        for (Map<String,String> entry : entries) {
            dataEntries.add(new ValueDataEntry(entry.get("date"),
                    Integer.parseInt(Objects.requireNonNull(entry.get("steps")))));
        }
        // Apply updated data to the chart
        cartesian.data(dataEntries);
    }

}