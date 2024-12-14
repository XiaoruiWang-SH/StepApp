package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DataBaseHelper databaseHelper;
    private TextView totalRunningValue, weeklyRunningValue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        ConstraintLayout root = binding.getRoot();

        // 初始化数据库助手
        databaseHelper = DataBaseHelper.getInstance(getContext());

        // 显示总跑步距离
        totalRunningValue = binding.totalRunningValue;
        double totalDistance = databaseHelper.getTotalDistance();
        totalRunningValue.setText(String.format("%.2f KM", totalDistance));

        // 显示本周跑步距离
        weeklyRunningValue = binding.weeklyRunningValue;
        double weeklyDistance = databaseHelper.getWeeklyDistance();
        weeklyRunningValue.setText(String.format("%.2f KM", weeklyDistance));

        // 设置圆按钮的点击事件
        binding.circleButton.setOnClickListener(view -> {
            // 获取 NavController 实例并进行跳转
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_homeFragment_to_newRunFragment);
        });

        TextView marathonTrainingPlan = root.findViewById(R.id.trainingPlanValue);

        // 设置点击监听器，跳转到 TrainingFragment
        marathonTrainingPlan.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_homeFragment_to_trainingFragment);
        });

        TextView enduranceTrainingPlan = root.findViewById(R.id.distanceTrainingValue);

        // 设置点击监听器，跳转到 TrainingFragment
        enduranceTrainingPlan.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_homeFragment_to_trainingFragment);
        });

        TextView sprintTrainingValue = root.findViewById(R.id.sprintTrainingValue);
        // 设置点击监听器，跳转到 TrainingFragment
        sprintTrainingValue.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_homeFragment_to_trainingFragment);
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 检查参数并导航到 NewRunFragment
        if (getArguments() != null && getArguments().getBoolean("openNewRun", false)) {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new NewRunFragment())
                    .addToBackStack(null)
                    .commit();

            // 清除参数，防止重复导航
            getArguments().remove("openNewRun");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
