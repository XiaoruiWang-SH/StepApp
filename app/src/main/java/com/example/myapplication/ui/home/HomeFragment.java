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
import com.example.myapplication.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        ConstraintLayout root = binding.getRoot();
        // 设置圆按钮的点击事件
        binding.circleButton.setOnClickListener(view -> {
            // 获取 NavController 实例并进行跳转
            NavController navController = Navigation.findNavController(view);
            // 确保这行代码中的 action ID 和 nav_graph.xml 中定义的 action 一致
            navController.navigate(R.id.action_homeFragment_to_newRunFragment);
        });
        // 在 HomeFragment.java 的 onCreateView() 方法中
        TextView trainingPlanTextView = binding.trainingPlanValue;
        trainingPlanTextView.setOnClickListener(view -> {

            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_homeFragment_to_newTrainingPlanFragment);
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
