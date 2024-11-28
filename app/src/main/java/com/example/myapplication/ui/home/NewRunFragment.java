package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;

import com.example.myapplication.databinding.FragmentNewRunBinding; // 假设你会创建一个新的 Binding 类

public class NewRunFragment extends Fragment {

    private FragmentNewRunBinding binding;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewRunBinding.inflate(inflater, container, false);
        // 你可以在这里进行页面的初始化，比如显示文本，按钮等
        binding.textView.setText("Welcome to the New Run page!");
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
