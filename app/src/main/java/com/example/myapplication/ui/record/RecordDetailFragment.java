package com.example.myapplication.ui.record;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
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
import java.util.ArrayList;
import java.util.Map;

import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.content.pm.PackageManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.myapplication.ui.record.SummedView;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;





public class RecordDetailFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private GoogleMap mMap;

    private FusedLocationProviderClient fusedLocationClient;

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

        // Initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        final TextView title_overview = binding.titleOverview;
        mViewModel.getTitle().observe(getViewLifecycleOwner(), title_overview::setText);

        SummedView summedView = new SummedView(getContext(), SummedView.TYPE.DAILY);
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

        // Define the route points
        List<LatLng> routePoints = new ArrayList<>();
//        46.0054, 8.9556
//        46.0048, 8.9556
//        46.0039, 8.9556
//        46.0036, 8.9537
//        46.0035, 8.9526
//        46.0028, 8.9507
//        46.0016, 8.9500
//        46.0003, 8.9496
//        45.9994, 8.9495
//        45.9986, 8.9486
//        45.9980, 8.9479
//        45.9973, 8.9476
//        45.9965, 8.9472
//        45.9953, 8.9467
//        45.9944, 8.9464
//        45.9932, 8.9468
//        45.9924, 8.9474
//        45.9920, 8.9483
        routePoints.add(new LatLng(46.0054, 8.9556));
        routePoints.add(new LatLng(46.0048, 8.9556));
        routePoints.add(new LatLng(46.0039, 8.9556));
        routePoints.add(new LatLng(46.0036, 8.9537));
        routePoints.add(new LatLng(46.0035, 8.9526));
        routePoints.add(new LatLng(46.0028, 8.9507));
        routePoints.add(new LatLng(46.0016, 8.9500));
        routePoints.add(new LatLng(46.0003, 8.9496));
        routePoints.add(new LatLng(45.9994, 8.9495));
        routePoints.add(new LatLng(45.9986, 8.9486));
        routePoints.add(new LatLng(45.9980, 8.9479));
        routePoints.add(new LatLng(45.9973, 8.9476));
        routePoints.add(new LatLng(45.9965, 8.9472));
        routePoints.add(new LatLng(45.9953, 8.9467));
        routePoints.add(new LatLng(45.9944, 8.9464));
        routePoints.add(new LatLng(45.9932, 8.9468));
        routePoints.add(new LatLng(45.9924, 8.9474));
        routePoints.add(new LatLng(45.9920, 8.9483));



        // Draw the polyline on the map
        Polyline route = mMap.addPolyline(new PolylineOptions()
                .addAll(routePoints) // Add the list of LatLng points
                .width(10) // Set line width
                .color(Color.BLUE) // Set line color
                .geodesic(true)); // Enable geodesic (curved lines)

        // Create LatLngBounds to include all route points
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : routePoints) {
            builder.include(point);
        }

        // Build the LatLngBounds
        LatLngBounds bounds = builder.build();

        // Move the camera to fit the bounds with padding
        int padding = 100; // Offset from edges in pixels
        // Move the camera to the first point
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    onMapReady(mMap);
                }
            } else {
                Toast.makeText(requireContext(), "Location permission is required to display your current location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}