package com.example.myapplication.ui.record;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Utils.DateUtils;
import com.example.myapplication.Utils.JsonUtils;
import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.RunningRecord;
import com.example.myapplication.databinding.FragmentRecordBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordFragment extends Fragment {

    private RecordViewModel mViewModel;
    private FragmentRecordBinding binding;

//    private Button btn;

    public static RecordFragment newInstance() {
        return new RecordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_record, container, false);
        RecordViewModel recordViewModel =
                new ViewModelProvider(this).get(RecordViewModel.class);

        binding = FragmentRecordBinding.inflate(inflater, container, false);
        ConstraintLayout root = binding.getRoot();

        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        ConstraintLayout scrollViewRoot = new ConstraintLayout(getContext());
        scrollViewRoot.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        scrollView.addView(scrollViewRoot);
        root.addView(scrollView);

        addSubViews(scrollViewRoot);


//        btn = new Button(getContext());
//        btn.setId(View.generateViewId());
//        btn.setText("Click");
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.action_recordFragment_to_recordDetailFragment);
//            }
//        });
//        root.addView(btn);
//
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(root);
//
//        // add constraint
//        constraintSet.connect(btn.getId(),ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
//        constraintSet.connect(btn.getId(),ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
//        constraintSet.connect(btn.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
//        constraintSet.connect(btn.getId(),ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,0);
//        constraintSet.setHorizontalBias(btn.getId(),0.5f);
//        constraintSet.setVerticalBias(btn.getId(), 0.3f);
//
//        constraintSet.applyTo(root);

//        NavController navController = Navigation.findNavController(root.getRootView());
//        NavHostFragment.findNavController(this);

        return root;

    }

    private void addSubViews(ConstraintLayout scrollViewRoot) {
        // create a text view
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(Color.RED);
        textView.setId(View.generateViewId());
        textView.setText("This is a Map here");
        textView.setTextSize(20);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                800
        );
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        textView.setLayoutParams(params);
        scrollViewRoot.addView(textView);


        // create a button
        Button button = new Button(getContext());
        button.setId(View.generateViewId());
        button.setText("This is a button");


        ConstraintLayout.LayoutParams buttonParams = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        buttonParams.topToBottom = textView.getId();
        buttonParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        buttonParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        buttonParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        button.setLayoutParams(buttonParams);
        scrollViewRoot.addView(button);

        // add constraint
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(scrollViewRoot);
//        constraintSet.connect(textView.getId(),ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
////        constraintSet.connect(textView.getId(),ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
//        constraintSet.connect(textView.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
//        constraintSet.connect(textView.getId(),ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,0);
//        constraintSet.constrainHeight(textView.getId(), 1000);
//
//        constraintSet.connect(button.getId(),ConstraintSet.TOP, textView.getId(), ConstraintSet.BOTTOM,0);
//        constraintSet.connect(button.getId(),ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
//        constraintSet.connect(button.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
//        constraintSet.connect(button.getId(),ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,0);
//
//        constraintSet.applyTo(scrollViewRoot);

    }



    @Override
    public void onStart() {
        super.onStart();
//        public void addRecord(Context context, String day, String month, String year, String place, String trainingDuration, String calories, String distance, String averageSpeed, String detailKms, String mapInfo)
        String timestamp = DateUtils.getCurrentTimestamp();
        String day = DateUtils.getCurrentDay();
        String month = DateUtils.getCurrentMonth();
        String year = DateUtils.getCurrentYear();
        String place = "Lugano";
        String trainingDuration = "1:30";
        String calories = "100";
        String distance = "10";
        String averageSpeed = "5";
        List<String> detailKms = new ArrayList<>();
        detailKms.add("6");
        detailKms.add("6.5");
        detailKms.add("6");
        detailKms.add("6.5");
        detailKms.add("5");
        String detailKms_json = JsonUtils.toJson(detailKms);


        List<Map<String,String>> mapInfo = new ArrayList<>();
        mapInfo.add(Map.of("lat","60","lng","90"));
        mapInfo.add(Map.of("lat","61","lng","91"));
        mapInfo.add(Map.of("lat","62","lng","92"));
        mapInfo.add(Map.of("lat","63","lng","93"));
        mapInfo.add(Map.of("lat","64","lng","94"));

        String mapInfo_json = JsonUtils.toJson(mapInfo);


        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getContext());
        dataBaseHelper.addRecord(getContext(), timestamp, day, month, year, place, trainingDuration, calories, distance, averageSpeed, detailKms_json, mapInfo_json);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
