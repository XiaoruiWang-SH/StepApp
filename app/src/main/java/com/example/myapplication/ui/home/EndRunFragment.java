package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;

public class EndRunFragment extends Fragment {

    public EndRunFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_end_run, container, false);

        // 找到按钮和文本 (Continue)
        View circleButtonEnd = view.findViewById(R.id.circleButton_end);
        TextView circleButtonEndText = view.findViewById(R.id.circleButtonEndText);

        // 找到按钮和文本 (End Run)
        View endButton = view.findViewById(R.id.endButton);
        TextView endButtonText = view.findViewById(R.id.endButtonText);

        // 使用数组封装变量，确保其是 effectively final
        final boolean[] isPaused = {false};
        final long[] pausedTime = {0};
        final double[] totalDistance = {0.0};

        // 获取传递来的暂停状态数据
        Bundle args = getArguments();
        if (args != null) {
            isPaused[0] = args.getBoolean("isPaused", false);
            pausedTime[0] = args.getLong("pausedTime", 0);
            totalDistance[0] = args.getDouble("totalDistance", 0.0);
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
            // 返回到主页
            Navigation.findNavController(v).navigate(R.id.action_endRunFragment_to_homeFragment);
        });

        return view;
    }
}
