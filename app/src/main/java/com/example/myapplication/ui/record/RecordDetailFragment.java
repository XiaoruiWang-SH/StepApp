package com.example.myapplication.ui.record;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.RunningRecord;
import com.example.myapplication.databinding.FragmentDetailRecordBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.myapplication.ui.record.SummedView;

public class RecordDetailFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private RecordDetailViewModel mViewModel;

    private FragmentDetailRecordBinding binding;

    public static RecordDetailFragment newInstance() {
        return new RecordDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_record_detail, container, false);
        binding = FragmentDetailRecordBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(RecordDetailViewModel.class);
        ConstraintLayout root = binding.getRoot();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        final TextView title_overview = binding.titleOverview;
        mViewModel.getTitle().observe(getViewLifecycleOwner(), title_overview::setText);

        SummedView summedView = new SummedView(getContext());
//        summedView.setBackgroundColor(Color.RED);
        summedView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams summedViewParams = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        summedViewParams.topToBottom = binding.mapFragment.getId();
        summedViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        summedViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        summedViewParams.leftMargin = 8;
        summedView.setLayoutParams(summedViewParams);
        root.addView(summedView);

        TextView listTitleTextView = new TextView(getContext());
        listTitleTextView.setText("Details");
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
        listTitleTextViewParams.leftMargin = 8;
        listTitleTextView.setLayoutParams(listTitleTextViewParams);
        root.addView(listTitleTextView);



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
        root.addView(listView);

        View header = getLayoutInflater().inflate(R.layout.redorddetail_list_header, null);
        listView.addHeaderView(header);

        // Sample data for the ListView
        Map<String, String> lap1 = Map.of("lap", "1", "time", "10:00", "distance", "1.0", "pace", "10:00");
        Map<String, String> lap2 = Map.of("lap", "2", "time", "20:00", "distance", "2.0", "pace", "10:00");
        Map<String, String> lap3 = Map.of("lap", "3", "time", "30:00", "distance", "3.0", "pace", "10:00");
        Map<String, String> lap4 = Map.of("lap", "4", "time", "40:00", "distance", "4.0", "pace", "10:00");
        Map<String, String> lap5 = Map.of("lap", "5", "time", "50:00", "distance", "5.0", "pace", "10:00");
        Map<String, String> lap6 = Map.of("lap", "6", "time", "60:00", "distance", "6.0", "pace", "10:00");
        Map<String, String> lap7 = Map.of("lap", "7", "time", "70:00", "distance", "7.0", "pace", "10:00");
        Map<String, String> lap8 = Map.of("lap", "8", "time", "80:00", "distance", "8.0", "pace", "10:00");
        Map<String, String> lap9 = Map.of("lap", "9", "time", "90:00", "distance", "9.0", "pace", "10:00");
        Map<String, String> lap10 = Map.of("lap", "10", "time", "100:00", "distance", "10.0", "pace", "10:00");

        List<Map<String, String>> laps = List.of(lap1, lap2, lap3, lap4, lap5, lap6, lap7, lap8, lap9, lap10);
        DetailRecordItem detailRecordItem = new DetailRecordItem(getContext(), laps);

        // Attach the adapter to the ListView
        listView.setAdapter(detailRecordItem);

        // Handle item clicks
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            String selectedItem = laps.get(position).get("lap");
//            Toast.makeText(getContext(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
//
////            NavController navController = Navigation.findNavController(view);
//            // 确保这行代码中的 action ID 和 nav_graph.xml 中定义的 action 一致
////            navController.navigate(R.id.action_recordFragment_to_recordDetailFragment);
//        });




        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        List<RunningRecord> runningRecords = dataBaseHelper.loadAllRecords(getContext());
        Log.d("RecordDetailFragment", "runningRecords: " + runningRecords.stream().count());


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date yesterday = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<RunningRecord> recordsFormYesterday = dataBaseHelper.loadRecordsByTimestamp(getContext(), sdf.format(yesterday));
        Log.d("RecordDetailFragment", "recordsFormYesterday: " + recordsFormYesterday.stream().count());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Example: Add a marker
        LatLng exampleLocation = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(exampleLocation).title("Marker in Sydney"));

        // Move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(exampleLocation, 10));
    }
}