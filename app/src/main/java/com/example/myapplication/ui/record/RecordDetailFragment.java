package com.example.myapplication.ui.record;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.RunningRecord;
import com.example.myapplication.databinding.FragmentDetailRecordBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public void onStart() {
        super.onStart();

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        List<RunningRecord> runningRecords = dataBaseHelper.loadAllRecords(getContext());
        Log.d("RecordDetailFragment", "runningRecords: " + runningRecords.stream().count());


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date yesterday = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<RunningRecord> recordsFormYesterday = dataBaseHelper.loadRecordsByTimestamp(getContext(), sdf.format(yesterday));
        Log.d("RecordDetailFragment", "recordsFormYesterday: " + recordsFormYesterday.stream().count());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


}