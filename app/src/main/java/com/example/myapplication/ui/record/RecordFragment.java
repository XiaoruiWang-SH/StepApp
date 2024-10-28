package com.example.myapplication.ui.record;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.databinding.FragmentRecordBinding;
import com.example.myapplication.ui.home.HomeViewModel;

public class RecordFragment extends Fragment {

    private FragmentRecordBinding binding;

    private Button btn;

    public static RecordFragment newInstance() {
        return new RecordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        NavController navController = Navigation.findNavController(requireActivity(), R.id.navigation_record);
//        NavigationUI.setupActionBarWithNavController((AppCompatActivity) requireActivity(), navController);
//        return inflater.inflate(R.layout.fragment_record, container, false);
        RecordViewModel recordViewModel =
                new ViewModelProvider(this).get(RecordViewModel.class);

        binding = FragmentRecordBinding.inflate(inflater, container, false);
        ConstraintLayout root = binding.getRoot();



        final TextView textView = binding.textRecord;
        recordViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        btn = new Button(getContext());
        btn.setId(View.generateViewId());
        btn.setText("Click");
        root.addView(btn);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(root);

        // add constraint
        constraintSet.connect(btn.getId(),ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
        constraintSet.connect(btn.getId(),ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
        constraintSet.connect(btn.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
        constraintSet.connect(btn.getId(),ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,0);
        constraintSet.setHorizontalBias(btn.getId(),0.5f);
        constraintSet.setVerticalBias(btn.getId(), 0.3f);

        constraintSet.applyTo(root);

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}