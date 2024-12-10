package com.example.myapplication.ui.home;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.navigation.Navigation;
import com.example.myapplication.R;

public class NewRunFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView runDistance, estimatedTime, currentPace;

    private LocationManager locationManager;
    private Location lastLocation;
    private long startTime;
    private double totalDistance = 0.0;

    private boolean isPaused = false; // 是否暂停状态
    private long pausedTime = 0; // 暂停时的时间
    private Handler handler = new Handler(Looper.getMainLooper()); // 主线程 Handler
    private Runnable updateTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_run, container, false);

        // 获取UI组件
        progressBar = view.findViewById(R.id.progressBar);
        runDistance = view.findViewById(R.id.runDistance);
        estimatedTime = view.findViewById(R.id.estimatedTime);
        currentPace = view.findViewById(R.id.currentPace);

        // 初始化位置管理器
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        // 检查是否从暂停状态恢复
        if (getArguments() != null) {
            isPaused = getArguments().getBoolean("isPaused", false);
            pausedTime = getArguments().getLong("pausedTime", 0);
            totalDistance = getArguments().getDouble("totalDistance", 0.0);

            if (isPaused) {
                startTime = System.currentTimeMillis() - pausedTime;
            } else {
                startTime = System.currentTimeMillis();
            }
        } else {
            startTime = System.currentTimeMillis(); // 第一次启动
        }

        // 启动或恢复更新
        startTracking();

        // 设置暂停按钮的点击事件
        View pauseButton = view.findViewById(R.id.circleButton_pause);
        pauseButton.setOnClickListener(v -> {
            stopTracking();

            // 准备传递暂停状态数据到下一个页面
            Bundle bundle = new Bundle();
            bundle.putBoolean("isPaused", true);
            bundle.putLong("pausedTime", System.currentTimeMillis() - startTime);
            bundle.putDouble("totalDistance", totalDistance);

            // 导航到暂停页面
            Navigation.findNavController(v).navigate(R.id.action_newRunFragment_to_endRunFragment, bundle);
        });

        return view;
    }

    private void startTracking() {
        isPaused = false;

        // 启动位置更新
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000, // 每秒更新一次
                    1,    // 移动 1 米更新一次
                    locationListener
            );
        }

        // 启动定时任务，每秒更新一次
        updateTask = new Runnable() {
            @Override
            public void run() {
                updateUI();
                handler.postDelayed(this, 1000); // 每秒执行一次
            }
        };
        handler.post(updateTask);
    }

    private void stopTracking() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        if (handler != null && updateTask != null) {
            handler.removeCallbacks(updateTask);
        }
        isPaused = true;
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // 计算距离
            if (lastLocation != null) {
                float distance = lastLocation.distanceTo(location);
                totalDistance += distance;
            }
            lastLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(@NonNull String provider) {}

        @Override
        public void onProviderDisabled(@NonNull String provider) {}
    };

    private void updateUI() {
        // 计算经过的时间
        long elapsedTime = System.currentTimeMillis() - startTime;
        double elapsedTimeInSeconds = elapsedTime / 1000.0;

        // 计算速度（米/秒）
        double speed = totalDistance / elapsedTimeInSeconds;

        // 更新文本视图
        runDistance.setText(String.format("Distance: %.2f m", totalDistance));
        estimatedTime.setText(String.format("Time: %.2f s", elapsedTimeInSeconds));
        currentPace.setText(String.format("Speed: %.2f m/s", speed));

        // 根据完成进度更新进度条
        double progress = (totalDistance / 5000.0) * 100; // 假设目标是5公里
        progressBar.setProgress((int) progress);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 停止位置更新和定时任务
        stopTracking();
    }
}


