package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragmentNewTrainingPlanBinding;

public class NewTrainingPlanFragment extends Fragment {

    private FragmentNewTrainingPlanBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewTrainingPlanBinding.inflate(inflater, container, false);

        // 显示欢迎信息或其他训练计划信息
        binding.trainingPlanText.setText("Welcome to the new Training Plan page!");

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
