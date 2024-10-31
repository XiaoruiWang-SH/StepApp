package com.example.myapplication.ui.record;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDetailRecordBinding;

public class RecordDetailFragment extends Fragment {

    private RecordDetailViewModel mViewModel;

    private FragmentDetailRecordBinding binding;

    public static RecordDetailFragment newInstance() {
        return new RecordDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_record_detail, container, false);
        binding = FragmentDetailRecordBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(RecordDetailViewModel.class);
        ConstraintLayout root = binding.getRoot();

        final TextView textView = binding.textRecordDetail;
        mViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // create a button to navigate back to the RecordFragment
        Button btn = new Button(getContext());
        btn.setId(View.generateViewId());
        btn.setText("Back");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_recordDetailFragment_to_recordFragment);
            }
        });
        root.addView(btn);
        

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


}