package com.example.myapplication.ui.home;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.RunningRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EndRunFragment extends Fragment {

    private static final double USER_WEIGHT = 70.0; // 用户体重 (kg)

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

        // 找到按钮 (Continue 和 End Run)
        View circleButtonEnd = view.findViewById(R.id.circleButton_end);
        View endButton = view.findViewById(R.id.endButton);

        // 初始化数据库助手
        DataBaseHelper databaseHelper = DataBaseHelper.getInstance(requireContext());

        // 使用数组封装变量，确保其是 effectively final
        final boolean[] isPaused = {false};
        final long[] pausedTime = {0};
        final double[] totalDistance = {0.0};
        final String[] endLocation = {""};

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
            runDistanceTextView.setText(String.format("Distance: %.2f m", totalDistance[0]));
            actualTimeTextView.setText(String.format("Actual Time: %.2f s", elapsedTimeInSeconds));
            currentPaceTextView.setText(String.format("Average Pace: %.2f s/m", averagePace));
        }

        // 为 Continue 按钮设置点击事件
        circleButtonEnd.setOnClickListener(v -> {
            // 准备返回数据
            Bundle bundle = new Bundle();
            bundle.putBoolean("isPaused", isPaused[0]);
            bundle.putLong("pausedTime", pausedTime[0]);
            bundle.putDouble("totalDistance", totalDistance[0]);

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
                    currentTime, // 时间戳
                    day, // 日
                    month, // 月
                    year, // 年
                    "", // 位置
                    String.format("%.2f", elapsedTimeInSeconds), // 持续时间
                    String.format("%.2f", caloriesBurned), // 卡路里
                    String.format("%.2f", totalDistance[0]), // 距离
                    String.format("%.2f", averagePace), // 平均配速
                    "", // 详细公里数 (如需要可添加)
                    ""  // 地图信息 (如需要可添加)
            );

            databaseHelper.addRecord(requireContext(), record);

            // 返回到主页
            Navigation.findNavController(v).navigate(R.id.action_endRunFragment_to_homeFragment);
        });

        return view;
    }

}

