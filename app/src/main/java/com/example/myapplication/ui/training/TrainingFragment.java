package com.example.myapplication.ui.training;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSettingBinding;
import com.example.myapplication.databinding.FragmentTrainingBinding;
import com.example.myapplication.ui.setting.SettingViewModel;

public class TrainingFragment extends Fragment {

    private TrainingViewModel mViewModel;
    private FragmentTrainingBinding binding;

    public static TrainingFragment newInstance() {
        return new TrainingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_training, container, false);

        TrainingViewModel trainingViewModel =
                new ViewModelProvider(this).get(TrainingViewModel.class);

        binding = FragmentTrainingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTraining;
        trainingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}