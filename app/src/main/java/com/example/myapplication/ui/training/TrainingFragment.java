package com.example.myapplication.ui.training;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.TrainingPlan;
import com.example.myapplication.databinding.FragmentTrainingBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TrainingFragment extends Fragment {

    private TrainingViewModel mViewModel;
    private FragmentTrainingBinding binding;
    private ViewPager2 viewPager;
    private FloatingActionButton fabUpdatePlan;
    private DataBaseHelper dataBaseHelper;
    private ImageView imageBackground; // 使用成员变量 ImageView 作为背景图片

    public static TrainingFragment newInstance() {
        return new TrainingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // 初始化数据库帮助类
        dataBaseHelper = DataBaseHelper.getInstance(requireContext());

        if (dataBaseHelper.loadAllTrainingPlans(requireContext()).isEmpty()) {
            dataBaseHelper.addMockTrainingPlans(requireContext());
        }

        // 获取 ViewModel 实例
        mViewModel = new ViewModelProvider(this).get(TrainingViewModel.class);

        // 绑定视图
        binding = FragmentTrainingBinding.inflate(inflater, container, false);
        ConstraintLayout root = binding.getRoot();

        // 获取 ImageView 作为背景图片，使用成员变量
        imageBackground = root.findViewById(R.id.imageBackground);

        // 初始化 ViewPager2
        viewPager = binding.viewPagerTraining;
        setupViewPager(viewPager);

        // 初始化 FloatingActionButton
        fabUpdatePlan = binding.fabUpdatePlan;
        fabUpdatePlan.setOnClickListener(v -> {
            // 点击按钮请求新的训练计划
            requestNewTrainingPlan();
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    // 配置 ViewPager2，设置适配器
    private void setupViewPager(ViewPager2 viewPager) {
        List<TrainingPlan> plans = dataBaseHelper.loadAllTrainingPlans(requireContext());
        TrainingPagerAdapter adapter = new TrainingPagerAdapter(this, plans);
        viewPager.setAdapter(adapter);
    }

    // 请求新的训练计划的方法
    private void requestNewTrainingPlan() {
        // 这里添加逻辑来请求 GPT-4 的训练计划
        // 可以使用网络请求库（例如 Retrofit）来实现请求
        // 请求完成后更新 ViewPager 中的数据
        mViewModel.requestTrainingPlan();
        // 更新适配器数据
        List<TrainingPlan> updatedPlans = dataBaseHelper.loadAllTrainingPlans(requireContext());
        ((TrainingPagerAdapter) viewPager.getAdapter()).updateData(updatedPlans);
    }
}
