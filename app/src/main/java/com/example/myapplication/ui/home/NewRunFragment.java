package com.example.myapplication.ui.home;

import android.Manifest;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class NewRunFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView runDistance, estimatedTime, currentPace;
    private TextView splitsTextView; // 用于显示每公里的配速和位置

    private long lastRecordTime;      // 上一次记录的时间戳（毫秒）
    private double lastRecordDistance; // 上一次记录的总距离（米）

    private LocationManager locationManager;
    private Location lastLocation;
    private long startTime;
    private double totalDistance = 0.0;

    private boolean isPaused = false; // 是否暂停状态
    private long pausedTime = 0; // 暂停时的时间
    private Handler handler = new Handler(Looper.getMainLooper()); // 主线程 Handler
    private Runnable updateTask;

    // 每公里的目标距离（米）
    private static final double KILOMETER = 1000.0;
    private int nextKilometer = 1; // 下一个公里标

    // 存储每公里的配速和位置
    private List<Double> paces = new ArrayList<>();
    private List<Map<String, String>> mapInfoList = new ArrayList<>();

    // 格式化小数点
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    // Gson实例用于JSON序列化
    private Gson gson = new GsonBuilder().create();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_run, container, false);

        // 初始化UI组件
        progressBar = view.findViewById(R.id.progressBar);
        runDistance = view.findViewById(R.id.runDistance);
        estimatedTime = view.findViewById(R.id.estimatedTime);
        currentPace = view.findViewById(R.id.currentPace);
        splitsTextView = view.findViewById(R.id.splitsTextView); // 用于显示每公里数据

        // 初始化位置管理器
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        // 检查是否从暂停状态恢复
        if (getArguments() != null) {
            isPaused = getArguments().getBoolean("isPaused", false);
            pausedTime = getArguments().getLong("pausedTime", 0);
            totalDistance = getArguments().getDouble("totalDistance", 0.0);
            paces = (List<Double>) getArguments().getSerializable("paces");
            mapInfoList = (List<Map<String, String>>) getArguments().getSerializable("mapInfoList");

            if (isPaused) {
                startTime = System.currentTimeMillis() - pausedTime;
            } else {
                startTime = System.currentTimeMillis();
            }
        } else {
            startTime = System.currentTimeMillis(); // 初次启动
        }

        // 启动或恢复跟踪
        startTracking();

        // 设置暂停按钮的点击事件
        View pauseButton = view.findViewById(R.id.circleButton_pause);
        pauseButton.setOnClickListener(v -> {
            stopTracking();

            // 组装记录数据
            Map<String, String> record = assembleRecord();
            String pacesJson = gson.toJson(paces);
            String mapInfoJson = gson.toJson(mapInfoList);

            // 准备传递暂停状态数据到下一个页面
            Bundle bundle = new Bundle();
            bundle.putBoolean("isPaused", true);
            bundle.putLong("pausedTime", System.currentTimeMillis() - startTime);
            bundle.putDouble("totalDistance", totalDistance);
            bundle.putSerializable("paces", (Serializable) paces);
            bundle.putString("paces", pacesJson);
            bundle.putString("mapInfo", mapInfoJson);

            // 导航到暂停页面
            Navigation.findNavController(v).navigate(R.id.action_newRunFragment_to_endRunFragment, bundle);
        });

        return view;
    }

    /**
     * 启动位置跟踪和UI更新任务
     */
    private void startTracking() {
        isPaused = false;

        // 启动位置更新
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000, // 每秒更新一次
                    1,    // 移动 1 米更新一次
                    locationListener
            );
        }

        // 启动定时任务，每10秒更新一次
        updateTask = new Runnable() {
            @Override
            public void run() {
                updateUI();
                handler.postDelayed(this, 10000); // 每10秒执行一次
            }
        };
        handler.post(updateTask);
    }

    /**
     * 停止位置跟踪和UI更新任务
     */
    private void stopTracking() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        if (handler != null && updateTask != null) {
            handler.removeCallbacks(updateTask);
        }
        isPaused = true;
    }

    /**
     * 位置监听器，监测位置变化
     */
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // 计算距离
            if (lastLocation != null) {
                float distance = lastLocation.distanceTo(location);
                totalDistance += distance;
            }
            lastLocation = location;

            long currentTime = System.currentTimeMillis();

            // 检查是否达到了下一个公里标
            if (totalDistance >= nextKilometer * KILOMETER) {
                double elapsedTime = (currentTime - startTime) / 1000.0; // 秒
                double pace = (elapsedTime / (nextKilometer * KILOMETER)) * 60.0; // 每公里分钟数

                // 获取当前坐标
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // 添加配速到 paces 列表
                paces.add(pace);

                // 添加坐标到 mapInfoList
                Map<String, String> coord = new HashMap<>();
                coord.put("lat", decimalFormat.format(latitude));
                coord.put("lng", decimalFormat.format(longitude));
                mapInfoList.add(coord);

                // 更新UI显示分割数据
                appendSplitToUI(nextKilometer, pace, latitude, longitude);

                // 更新下一个公里标
                nextKilometer++;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // API 29已弃用
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            // 处理提供者启用
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            // 处理提供者禁用
        }
    };

    /**
     * 更新UI显示当前距离、时间和速度
     */
    private void updateUI() {
        // 计算经过的时间
        long elapsedTime = System.currentTimeMillis() - startTime;
        double elapsedTimeInSeconds = elapsedTime / 1000.0;

        // 计算速度（米/秒）
        double speed = totalDistance / elapsedTimeInSeconds;

        // 更新文本视图
        runDistance.setText(String.format("Distance: %.1f m", totalDistance));
        estimatedTime.setText(String.format("Time: %.1f s", elapsedTimeInSeconds));
        currentPace.setText(String.format("Speed: %.1f m/s", speed));

        // 根据完成进度更新进度条
        double progress = (totalDistance / 5000.0) * 100; // 假设目标是5公里
        if (progress > 100) progress = 100;
        progressBar.setProgress((int) progress);
    }

    /**
     * 将每公里的配速和位置追加到UI中
     *
     * @param kilometer 当前公里数
     * @param pace      当前公里配速
     * @param latitude  当前纬度
     * @param longitude 当前经度
     */
    private void appendSplitToUI(int kilometer, double pace, double latitude, double longitude) {
        // 格式化配速
        int minutes = (int) (pace / 60);
        double seconds = pace % 60;
        String paceStr = String.format(Locale.getDefault(), "%d:%02.0f min/km", minutes, seconds);

        // 格式化坐标
        String locationStr = String.format(Locale.getDefault(), "Lat: %.5f, Lon: %.5f", latitude, longitude);

        // 追加分割信息到 splitsTextView
        String splitInfo = String.format("Kilometer %d - Pace: %s, Location: %s\n",
                kilometer, paceStr, locationStr);
        splitsTextView.append(splitInfo);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 当片段暂停时，停止位置更新和定时任务
        stopTracking();
    }

    /**
     * 组装记录数据到Map<String, String>中
     *
     * @return 组装好的记录Map
     */
    private Map<String, String> assembleRecord() {
        Map<String, String> record = new HashMap<>();

        // 生成唯一ID
        String id = UUID.randomUUID().toString();
        record.put("id", id);

        // 获取当前时间和日期
        Date now = new Date();
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = timestampFormat.format(now);
        record.put("timestamp", timestamp);

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String day = dayFormat.format(now);
        record.put("day", day);

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        String month = monthFormat.format(now);
        record.put("month", month);

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        String year = yearFormat.format(now);
        record.put("year", year);

        // 获取地点，这里默认设为"Lugano"，可根据需要修改
        String place = "Lugano";
        record.put("Lugano", place);

        // 计算训练时长
        long elapsedTime = System.currentTimeMillis() - startTime;
        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long seconds = (elapsedTime / 1000) % 60;
        String trainingDuration = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        record.put("trainingDuration", trainingDuration);

        // 计算消耗的卡路里，这里使用简单的公式，实际可根据需要调整
        double weightInKg = 70.0; // 假设体重为70kg，可根据实际情况获取
        double caloriesBurned = weightInKg * (totalDistance / 1000.0) * 1.036; // 简单公式
        record.put("calories", String.valueOf((int) caloriesBurned));

        // 总距离取整
        int randomDistance = (int) Math.round(totalDistance);
        record.put("distance", Integer.toString(randomDistance));

        // 计算平均速度（米/秒）
        double averageSpeed = totalDistance / (elapsedTime / 1000.0);
        String averageSpeedStr = String.format(Locale.getDefault(), "%.2f", averageSpeed);
        record.put("averageSpeed", averageSpeedStr);

        // 将配速列表转换为JSON字符串
        String detailKms = gson.toJson(paces);
        record.put("detailKms", detailKms);

        // 将坐标列表转换为JSON字符串
        String mapInfo = gson.toJson(mapInfoList);
        record.put("mapInfo", mapInfo);

        return record;
    }

    /**
     * 静态内部类，用于存储每公里的配速和位置
     */
    public static class KilometerSplit implements Serializable {
        private int kilometer;
        private double pace; // 每公里配速，单位分钟
        private double latitude;
        private double longitude;

        public KilometerSplit(int kilometer, double pace, double latitude, double longitude) {
            this.kilometer = kilometer;
            this.pace = pace;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public int getKilometer() {
            return kilometer;
        }

        public double getPace() {
            return pace;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
