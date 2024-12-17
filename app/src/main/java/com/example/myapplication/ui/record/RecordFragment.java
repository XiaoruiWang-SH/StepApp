package com.example.myapplication.ui.record;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Utils.DateUtils;
import com.example.myapplication.Utils.JsonUtils;
import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.RunningRecord;
import com.example.myapplication.databinding.FragmentRecordBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordFragment extends Fragment implements StatisticView.StatisticViewListener {

    private RecordViewModel mViewModel;
    private FragmentRecordBinding binding;

    private StatisticView statisticView;
    private SummedView summedView;

    private StatisticView.StatisticType statisticType = StatisticView.StatisticType.WEEK;
    List<RunningRecord> listDataSource = new ArrayList<>();
    ActivityRecordItem activityRecordItem;

    public static RecordFragment newInstance() {
        return new RecordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_record, container, false);
        RecordViewModel recordViewModel =
                new ViewModelProvider(this).get(RecordViewModel.class);

        binding = FragmentRecordBinding.inflate(inflater, container, false);
        ConstraintLayout root = binding.getRoot();

        addSubViews(root);

        return root;

    }

    private void addSubViews(ConstraintLayout scrollViewRoot) {
        // create a text view
        int height = Resources.getSystem().getDisplayMetrics().heightPixels / 4;
        statisticView = new StatisticView(getContext(), height);
        // Set an ID for StatisticView
        statisticView.setId(View.generateViewId()); // Generate a unique ID for StatisticView
        statisticView.setStatisticViewListener(this);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height // Full screen height in pixels

        );
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;

        statisticView.setLayoutParams(params);
        scrollViewRoot.addView(statisticView);


        summedView = new SummedView(getContext(), SummedView.TYPE.OVERALL);
//        summedView.setBackgroundColor(Color.RED);
        summedView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams summedViewParams = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        summedViewParams.topToBottom = statisticView.getId();
        summedViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        summedViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        summedView.setLayoutParams(summedViewParams);
        scrollViewRoot.addView(summedView);

        TextView listTitleTextView = new TextView(getContext());
        listTitleTextView.setText("Running Activities");
        listTitleTextView.setId(View.generateViewId());
        listTitleTextView.setTextSize(14);
        listTitleTextView.setTextColor(Color.BLACK);
        ConstraintLayout.LayoutParams listTitleTextViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        listTitleTextViewParams.topToBottom = summedView.getId();
        listTitleTextViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        listTitleTextViewParams.topMargin = 20;
        listTitleTextView.setLayoutParams(listTitleTextViewParams);
        scrollViewRoot.addView(listTitleTextView);


        // Create a ListView programmatically
        ListView listView = new ListView(getContext());
//        listView.setBackgroundColor(Color.LTGRAY);
        listView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams listViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        );
        listViewParams.topToBottom = listTitleTextView.getId();
        listViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        listViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        listViewParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        listViewParams.topMargin = 20;
        listView.setLayoutParams(listViewParams);
        scrollViewRoot.addView(listView);

        // Sample data for the ListView
        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 1", "Item 2", "Item 3", "Item 4", "Item 1", "Item 2", "Item 3", "Item 4"};

        RunningRecord record1 = new RunningRecord(1, "2024-10-26", "26", "10", "2024", "Lugano", "1:30", "100", "5", "5", "[6,6.5,6,6.5,5]", "[{\"lat\":\"60\",\"lng\":\"90\"},{\"lat\":\"61\",\"lng\":\"91\"},{\"lat\":\"62\",\"lng\":\"92\"},{\"lat\":\"63\",\"lng\":\"93\"},{\"lat\":\"64\",\"lng\":\"94\"}]");
        RunningRecord record2 = new RunningRecord(2, "2024-10-25", "25", "10", "2024", "Shanghai", "1:30", "100", "6.5", "5", "[6,6.5,6,6.5,5]", "[{\"lat\":\"60\",\"lng\":\"90\"},{\"lat\":\"61\",\"lng\":\"91\"},{\"lat\":\"62\",\"lng\":\"92\"},{\"lat\":\"63\",\"lng\":\"93\"},{\"lat\":\"64\",\"lng\":\"94\"}]");
        RunningRecord record3 = new RunningRecord(3, "2024-10-24", "24", "10", "2024", "Zurigo", "1:30", "100", "8", "5", "[6,6.5,6,6.5,5]", "[{\"lat\":\"60\",\"lng\":\"90\"},{\"lat\":\"61\",\"lng\":\"91\"},{\"lat\":\"62\",\"lng\":\"92\"},{\"lat\":\"63\",\"lng\":\"93\"},{\"lat\":\"64\",\"lng\":\"94\"}]");
        RunningRecord record4 = new RunningRecord(4, "2024-10-23", "23", "10", "2024", "Lugano", "1:30", "100", "7", "5", "[6,6.5,6,6.5,5]", "[{\"lat\":\"60\",\"lng\":\"90\"},{\"lat\":\"61\",\"lng\":\"91\"},{\"lat\":\"62\",\"lng\":\"92\"},{\"lat\":\"63\",\"lng\":\"93\"},{\"lat\":\"64\",\"lng\":\"94\"}]");
        RunningRecord record5 = new RunningRecord(5, "2024-10-22", "22", "10", "2024", "Beijing", "1:30", "100", "10", "5", "[6,6.5,6,6.5,5]", "[{\"lat\":\"60\",\"lng\":\"90\"},{\"lat\":\"61\",\"lng\":\"91\"},{\"lat\":\"62\",\"lng\":\"92\"},{\"lat\":\"63\",\"lng\":\"93\"},{\"lat\":\"64\",\"lng\":\"94\"}]");
        RunningRecord record6 = new RunningRecord(6, "2024-10-21", "21", "10", "2024", "Lugano", "1:30", "100", "12", "5", "[6,6.5,6,6.5,5]", "[{\"lat\":\"60\",\"lng\":\"90\"},{\"lat\":\"61\",\"lng\":\"91\"},{\"lat\":\"62\",\"lng\":\"92\"},{\"lat\":\"63\",\"lng\":\"93\"},{\"lat\":\"64\",\"lng\":\"94\"}]");
        RunningRecord record7 = new RunningRecord(7, "2024-10-20", "20", "10", "2024", "Lugano", "1:30", "100", "7", "5", "[6,6.5,6,6.5,5]", "[{\"lat\":\"60\",\"lng\":\"90\"},{\"lat\":\"61\",\"lng\":\"91\"},{\"lat\":\"62\",\"lng\":\"92\"},{\"lat\":\"63\",\"lng\":\"93\"},{\"lat\":\"64\",\"lng\":\"94\"}]");
        RunningRecord record8 = new RunningRecord(8, "2024-10-19", "19", "10", "2024", "Lugano", "1:30", "100", "11", "5", "[6,6.5,6,6.5,5]", "[{\"lat\":\"60\",\"lng\":\"90\"},{\"lat\":\"61\",\"lng\":\"91\"},{\"lat\":\"62\",\"lng\":\"92\"},{\"lat\":\"63\",\"lng\":\"93\"},{\"lat\":\"64\",\"lng\":\"94\"}]");
        RunningRecord record9 = new RunningRecord(9, "2024-10-18", "18", "10", "2024", "Lugano", "1:30", "100", "7.9", "5", "[6,6.5,6,6.5,5]", "[{\"lat\":\"60\",\"lng\":\"90\"},{\"lat\":\"61\",\"lng\":\"91\"},{\"lat\":\"62\",\"lng\":\"92\"},{\"lat\":\"63\",\"lng\":\"93\"},{\"lat\":\"64\",\"lng\":\"94\"}]");

//        List<RunningRecord> listDataSource = List.of(record1, record2, record3, record4, record5, record6, record7, record8, record9);
        // Create an adapter for the ListView
        activityRecordItem = new ActivityRecordItem(getContext(), listDataSource);

        // Attach the adapter to the ListView
        listView.setAdapter(activityRecordItem);

        // Handle item clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = items[position];
            Toast.makeText(getContext(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();

            NavController navController = Navigation.findNavController(view);
            // 确保这行代码中的 action ID 和 nav_graph.xml 中定义的 action 一致
            navController.navigate(R.id.action_recordFragment_to_recordDetailFragment);
        });

    }



    @Override
    public void onStart() {
        super.onStart();

        refresh();
    }

    public void refresh(){
//        Toast.makeText(getContext(), "refershvdate", Toast.LENGTH_SHORT).show();
        onStatisticTypeChanged(statisticType);

    }


    private void configViewForType(StatisticView.StatisticType type) {
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        String startDate = getRecordStartDate(type);
        List<RunningRecord> records = dataBaseHelper.loadRecordsByTimestamp(getContext(), startDate);
        List<Map<String,String>> recordList =getReocrdsFromDates(statisticType);
        statisticView.refreshGraph(records, recordList);

        int totalDistance = 0;
        double totalDuration = 0.0;
        int totalCalories = 0;
        double avgSpeed = 0;
        for (  RunningRecord record: records) {
            totalDistance += Integer.parseInt(record.getDistance());
            totalDuration += Double.parseDouble(record.getTrainingDuration());
            totalCalories += Integer.parseInt(record.getCalories());
        }
        avgSpeed += totalDistance / totalDuration;
        String totalDurationStr = String.format("%.1f", totalDuration);
        String avgSpeedStr = String.format("%.1f", avgSpeed);

        summedView.configureView(SummedView.TYPE.OVERALL, List.of(
                Map.of("numberTextViewText", Integer.toString(totalDistance), "unitTextViewText", "km", "titleTextViewText", "Total Distance"),
                Map.of("numberTextViewText", totalDurationStr, "unitTextViewText", "hours", "titleTextViewText", "Total Duration"),
                Map.of("numberTextViewText", Integer.toString(totalCalories), "unitTextViewText", "cal", "titleTextViewText", "Total Calories"),
                Map.of("numberTextViewText", avgSpeedStr, "unitTextViewText", "km/h", "titleTextViewText", "Average Speed")));

        listDataSource.clear();
        listDataSource.addAll(records);
        activityRecordItem.notifyDataSetChanged();

    }


    @Override
    public void onStatisticTypeChanged(StatisticView.StatisticType type) {
        Toast.makeText(getContext(), "Statistic type changed to " + type, Toast.LENGTH_SHORT).show();

        switch (type){
            case WEEK:
            {
                statisticType = StatisticView.StatisticType.WEEK;
                configViewForType(statisticType);
            }

                break;
            case MONTH:
            {
                statisticType = StatisticView.StatisticType.MONTH;
                configViewForType(statisticType);
            }
                break;
            case YEAR:
                summedView.configureView(SummedView.TYPE.OVERALL, List.of(Map.of("numberTextViewText", "0", "unitTextViewText", "km", "titleTextViewText", "Total Distance"),
                        Map.of("numberTextViewText", "0", "unitTextViewText", "min", "titleTextViewText", "Total Duration"),
                        Map.of("numberTextViewText", "0", "unitTextViewText", "cal", "titleTextViewText", "Total Calories"),
                        Map.of("numberTextViewText", "0", "unitTextViewText", "km/h", "titleTextViewText", "Average Speed")));
                break;
        }
    }


    private List<Map<String,String>> getReocrdsFromDates(StatisticView.StatisticType type) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        // Store the dates in a list
        List<Map<String,String>> lastWeekDates = new ArrayList<>();

        int days = 7;
        switch (type){
            case WEEK:
                days = 7;
                break;
            case MONTH:
                days = 30;
                break;
            case YEAR:
                days = 12;
                break;
        }

        // Get the dates for the last 7 days
        for (int i = 0; i < days; i++) {
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



    private String getRecordStartDate(StatisticView.StatisticType type) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int backdays = 0;
        switch (type){
            case WEEK:
                backdays = 6;
                break;
            case MONTH:
                backdays = 29;
                break;
            case YEAR:
                backdays = 365;
                break;
        }
        calendar.add(Calendar.DAY_OF_YEAR, -backdays);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }

}
