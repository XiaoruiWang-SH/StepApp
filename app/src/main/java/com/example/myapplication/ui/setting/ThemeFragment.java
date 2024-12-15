package com.example.myapplication.ui.setting;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSettingBinding;
import com.example.myapplication.databinding.FragmentThemeBinding;

public class ThemeFragment extends Fragment {

    private ThemeChangeListener themeChangeListener;


    // Interface to communicate with the parent Activity
    public interface ThemeChangeListener {
        void onThemeChange(Theme theme);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ThemeChangeListener) {
            themeChangeListener = (ThemeChangeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ThemeChangeListener");
        }
    }


    private ThemeViewModel mViewModel;

    public static ThemeFragment newInstance() {
        return new ThemeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentThemeBinding binding = FragmentThemeBinding.inflate(inflater, container, false);
        binding.menuThemeDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("menuThemeDark", "onClick: Dark theme clicked");
//                setTheme(R.style.Theme_MyApplication_Dark);
                if (themeChangeListener != null) {
                    themeChangeListener.onThemeChange(Theme.DARK);
                }
            }

        });

        binding.menuThemeLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("menuThemeLight", "onClick: Light theme clicked");
                if (themeChangeListener != null) {
                    themeChangeListener.onThemeChange(Theme.LIGHT);
                }
            }

        });

        return binding.getRoot();


    }


}