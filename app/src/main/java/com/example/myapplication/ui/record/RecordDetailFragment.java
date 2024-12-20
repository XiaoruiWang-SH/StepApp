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

import org.json.JSONArray;
import org.json.JSONException;


public class RecordDetailFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private GoogleMap mMap;

    private SummedView summedView;

    private RunningRecord runningRecord;

    private FusedLocationProviderClient fusedLocationClient;

    private RecordDetailViewModel mViewModel;

    private FragmentDetailRecordBinding binding;

    List<Map<String, String>> laps = new ArrayList<>();
    DetailRecordItem detailRecordItem;
    List<LatLng> routePoints = new ArrayList<>();

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
        if (getArguments() != null) {
            runningRecord = getArguments().getParcelable("selectedItem");
        }

        // Initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        final TextView title_overview = binding.titleOverview;
        mViewModel.getTitle().observe(getViewLifecycleOwner(), title_overview::setText);

        summedView = new SummedView(getContext(), SummedView.TYPE.DAILY);
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

        detailRecordItem = new DetailRecordItem(getContext(), laps);
        // Attach the adapter to the ListView
        listView.setAdapter(detailRecordItem);

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

        configview(runningRecord);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

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

    private void configview(RunningRecord runningRecord) {

        // summary view
        int distance = Integer.parseInt(runningRecord.getDistance());
        double duration = Double.parseDouble(runningRecord.getTrainingDuration());
        double pace = duration / distance;

        summedView.configureView(SummedView.TYPE.OVERALL, List.of(
                Map.of("numberTextViewText", runningRecord.getDistance(), "unitTextViewText", "km", "titleTextViewText", "Distance"),
                Map.of("numberTextViewText", runningRecord.getTrainingDuration(), "unitTextViewText", "h", "titleTextViewText", "Time"),
                Map.of("numberTextViewText", Double.toString(pace), "unitTextViewText", "/km", "titleTextViewText", "Pace"),
                Map.of("numberTextViewText", runningRecord.getCalories(), "unitTextViewText", "cal", "titleTextViewText", "Calories")

                ));


        // detail view
        String kms = runningRecord.getDetailKms();
        if (kms != null) {
            List<Map<String, String>> laplist = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(kms);
                double time = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    String v = jsonArray.getString(i);
                    double lap_time = Double.parseDouble(v);
                    time += lap_time;

                    Map<String, String> lap = Map.of("lap", Integer.toString(i+1), "time", Double.toString(time), "distance", "1.0", "pace", v);
                    laplist.add(lap);
                    laps.clear();
                    laps.addAll(laplist);
                    detailRecordItem.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // map view
        String mapinfo = runningRecord.getMapInfo();
        if (mapinfo == null) {
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray(mapinfo);
            routePoints = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String lat = jsonArray.getJSONObject(i).getString("lat");
                String lng = jsonArray.getJSONObject(i).getString("lng");
                routePoints.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mMap != null) {
            mMap.clear();
            onMapReady(mMap);
        }
    }
}