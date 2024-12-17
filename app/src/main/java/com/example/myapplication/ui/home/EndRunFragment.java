package com.example.myapplication.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.RunningRecord;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EndRunFragment extends Fragment {

    private static final double USER_WEIGHT = 70.0; // 用户体重 (kg)
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private FusedLocationProviderClient fusedLocationClient;
    private String currentLocation = "";
    private TextView currentLocationTextView;
    private String pacesJson;
    private String mapInfoJson;


    public EndRunFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_end_run, container, false);

        // 找到显示距离、时间和配速的控件
        TextView runDistanceTextView = view.findViewById(R.id.runDistance);
        TextView actualTimeTextView = view.findViewById(R.id.actualTime);
        TextView currentPaceTextView = view.findViewById(R.id.currentPace);

        // 找到并初始化位置显示的 TextView
        currentLocationTextView = view.findViewById(R.id.currentLocation);
        currentLocationTextView.setText("Location: " + currentLocation);

        // 找到按钮 (Continue 和 End Run)
        View circleButtonEnd = view.findViewById(R.id.circleButton_end);
        View endButton = view.findViewById(R.id.endButton);

        // 初始化数据库助手
        DataBaseHelper databaseHelper = DataBaseHelper.getInstance(requireContext());

        // 初始化 FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // 使用数组封装变量，确保其是 effectively final
        final boolean[] isPaused = {false};
        final long[] pausedTime = {0};
        final double[] totalDistance = {0.0};
        // final String[] endLocation = {""}; // 移除数组，使用类成员变量

        // 获取传递来的暂停状态数据
        Bundle args = getArguments();
        if (args != null) {
            isPaused[0] = args.getBoolean("isPaused", false);
            pausedTime[0] = args.getLong("pausedTime", 0);
            totalDistance[0] = args.getDouble("totalDistance", 0.0);



            // 计算时间（秒）、平均配速（秒/米）、和卡路里消耗
            double elapsedTimeInSeconds = pausedTime[0] / 1000.0;
            double averagePace = totalDistance[0] > 0 ? elapsedTimeInSeconds / totalDistance[0] : 0;
            double caloriesBurned = totalDistance[0] / 1000.0 * USER_WEIGHT * 1.036; // 距离转为 km

            // 动态更新显示的内容
            runDistanceTextView.setText(String.format("Distance: %.1f m", totalDistance[0]));
            actualTimeTextView.setText(String.format("Actual Time: %.1f s", elapsedTimeInSeconds));
            currentPaceTextView.setText(String.format("Average Pace: %.1f s/m", averagePace));
        }

        // 请求位置权限
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }

        // 为 Continue 按钮设置点击事件
        circleButtonEnd.setOnClickListener(v -> {
            // 准备返回数据
            Bundle bundle = new Bundle();
            bundle.putBoolean("isPaused", isPaused[0]);
            bundle.putLong("pausedTime", pausedTime[0]);
            bundle.putDouble("totalDistance", totalDistance[0]);
            bundle.putString("paces", pacesJson);
            bundle.putString("mapInfo", mapInfoJson);

            // 导航回到 NewRunFragment
            Navigation.findNavController(v).navigate(R.id.action_endRunFragment_to_newRunFragment, bundle);
        });

        // 为 End Run 按钮设置点击事件
        endButton.setOnClickListener(v -> {
            // 写入跑步记录到数据库
            double elapsedTimeInSeconds = pausedTime[0] / 1000.0;
            double averagePace = totalDistance[0] > 0 ? elapsedTimeInSeconds / totalDistance[0] : 0;
            double caloriesBurned = totalDistance[0] / 1000.0 * USER_WEIGHT * 1.036; // 距离转为 km

            // 获取当前日期和时间
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String[] dateParts = currentDate.split("-");
            String year = dateParts[0];
            String month = dateParts[1];
            String day = dateParts[2];

            RunningRecord record = new RunningRecord(
                    0, // ID 自动生成
                    currentDate, // 时间戳
                    day, // 日
                    month, // 月
                    year, // 年
                    "Lugano", // 位置
                    String.format("%.2f", elapsedTimeInSeconds/3600), // 持续时间小时
                    String.format("%.2f", caloriesBurned), // 卡路里
                    String.format("%.2f", totalDistance[0]), // 距离
                    String.format("%.2f", averagePace), // 平均配速
                    String.format("%.2f", pacesJson), // 详细公里数 (如需要可添加)
                    String.format("%.2f", mapInfoJson)  // 地图信息 (如需要可添加)
            );

//            databaseHelper.addRecord(requireContext(), record);
            List<RunningRecord> list = databaseHelper.loadAllRecords(getContext());
            Log.d("aaa", list.toString());

            // 返回到主页
            Navigation.findNavController(v).navigate(R.id.action_endRunFragment_to_homeFragment);
        });

        return view;
    }

    /**
     * 获取最后已知的位置
     */
    private void getLastLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // 在这里处理位置
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                currentLocation = String.format(Locale.getDefault(), "%.6f, %.6f", latitude, longitude);
                                currentLocationTextView.setText("Location: Lugano\nAxis: " + currentLocation);
                            } else {
                                currentLocation = "Unknown";
                                currentLocationTextView.setText("Location: Unknown");
                                Toast.makeText(requireContext(), "无法获取当前位置", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "定位权限未授予", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理权限请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            boolean granted = true;
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    granted = granted && (result == PackageManager.PERMISSION_GRANTED);
                }
            } else {
                granted = false;
            }

            if (granted) {
                getLastLocation();
            } else {
                Toast.makeText(requireContext(), "定位权限被拒绝，无法记录位置", Toast.LENGTH_SHORT).show();
                currentLocation = "Permission Denied";
                currentLocationTextView.setText("Location: " + currentLocation);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 其他清理工作
    }
}

