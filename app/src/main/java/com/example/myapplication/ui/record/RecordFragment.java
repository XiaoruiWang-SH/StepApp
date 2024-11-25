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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordFragment extends Fragment {

    private RecordViewModel mViewModel;
    private FragmentRecordBinding binding;

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
        StatisticView statisticView = new StatisticView(getContext(), height);
        // Set an ID for StatisticView
        statisticView.setId(View.generateViewId()); // Generate a unique ID for StatisticView

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height // Full screen height in pixels

        );
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;

        statisticView.setLayoutParams(params);
        scrollViewRoot.addView(statisticView);


        SummedView summedView = new SummedView(getContext());
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


        ActivityRecordItem activityRecordItem = new ActivityRecordItem(getContext(), List.of(record1, record2, record3, record4, record5, record6, record7, record8, record9));

        // Attach the adapter to the ListView
        listView.setAdapter(activityRecordItem);

        // Handle item clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = items[position];
            Toast.makeText(getContext(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
        });

    }



    @Override
    public void onStart() {
        super.onStart();
//        public void addRecord(Context context, String day, String month, String year, String place, String trainingDuration, String calories, String distance, String averageSpeed, String detailKms, String mapInfo)
        String timestamp = DateUtils.getCurrentTimestamp();
        String day = DateUtils.getCurrentDay();
        String month = DateUtils.getCurrentMonth();
        String year = DateUtils.getCurrentYear();
        String place = "Lugano";
        String trainingDuration = "1:30";
        String calories = "100";
        String distance = "10";
        String averageSpeed = "5";
        List<String> detailKms = new ArrayList<>();
        detailKms.add("6");
        detailKms.add("6.5");
        detailKms.add("6");
        detailKms.add("6.5");
        detailKms.add("5");
        String detailKms_json = JsonUtils.toJson(detailKms);


        List<Map<String,String>> mapInfo = new ArrayList<>();
        mapInfo.add(Map.of("lat","60","lng","90"));
        mapInfo.add(Map.of("lat","61","lng","91"));
        mapInfo.add(Map.of("lat","62","lng","92"));
        mapInfo.add(Map.of("lat","63","lng","93"));
        mapInfo.add(Map.of("lat","64","lng","94"));

        String mapInfo_json = JsonUtils.toJson(mapInfo);


        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        dataBaseHelper.addRecord(getContext(), timestamp, day, month, year, place, trainingDuration, calories, distance, averageSpeed, detailKms_json, mapInfo_json);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
