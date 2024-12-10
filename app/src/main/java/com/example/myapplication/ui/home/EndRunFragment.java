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

        // 找到第一个按钮和文本
        View circleButtonEnd = view.findViewById(R.id.circleButton_end);
        TextView circleButtonEndText = view.findViewById(R.id.circleButtonEndText);

        // 找到第二个按钮和文本
        View endButton = view.findViewById(R.id.endButton);
        TextView endButtonText = view.findViewById(R.id.endButtonText);

        // 为按钮设置点击事件
        circleButtonEnd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_endRunFragment_to_newRunFragment);
        });

        endButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_endRunFragment_to_homeFragment);
        });

        return view;
    }
}
