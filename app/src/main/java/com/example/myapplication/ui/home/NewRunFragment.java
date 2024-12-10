package com.example.myapplication.ui.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.myapplication.R;

public class NewRunFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView runDistance;
    private TextView estimatedTime;
    private TextView currentPace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_run, container, false);

        // 获取UI组件
        progressBar = view.findViewById(R.id.progressBar);
        runDistance = view.findViewById(R.id.runDistance);
        estimatedTime = view.findViewById(R.id.estimatedTime);
        currentPace = view.findViewById(R.id.currentPace);

        // 更新UI（你可以根据需求获取这些数据）
        progressBar.setProgress(50); // 示例进度
        runDistance.setText("Distance: 10 km");
        estimatedTime.setText("Estimated Time: 45 minutes");
        currentPace.setText("Current Pace: 5:30 min/km");


        // 找到第二个按钮和文本
        View endButton = view.findViewById(R.id.circleButton_pause);
        TextView endButtonText = view.findViewById(R.id.runButtonText);

        // 为按钮设置点击事件
        endButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_newRunFragment_to_endRunFragment);
        });
        
        return view;
    }
}
