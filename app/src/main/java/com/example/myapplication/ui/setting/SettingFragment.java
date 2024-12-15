package com.example.myapplication.ui.setting;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_setting, container, false);

        SettingViewModel settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        ConstraintLayout root = binding.getRoot();

//        final TextView textView = binding.textTraining;
//        settingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

//        binding.fragmentContainerView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

        binding.menuLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SettingFragment", "Language clicked");
                NavController navController = Navigation.findNavController(v);
                // 确保这行代码中的 action ID 和 nav_graph.xml 中定义的 action 一致
                navController.navigate(R.id.action_settingFragment_to_languageFragment);
            }
        });

        binding.menuTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SettingFragment", "Theme clicked");
                NavController navController = Navigation.findNavController(v);
                // 确保这行代码中的 action ID 和 nav_graph.xml 中定义的 action 一致
                navController.navigate(R.id.action_settingFragment_to_themeFragment);
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}