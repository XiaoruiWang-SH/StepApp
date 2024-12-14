package com.example.myapplication.ui.training;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dataBase.TrainingPlan;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.home.NewRunFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class TrainingPagerAdapter extends RecyclerView.Adapter<TrainingPagerAdapter.TrainingViewHolder> {

    private final Fragment fragment;
    private List<TrainingPlan> plans;

    public TrainingPagerAdapter(Fragment fragment, List<TrainingPlan> plans) {
        this.fragment = fragment;
        this.plans = plans;
    }

    @NonNull
    @Override
    public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training_card, parent, false);
        return new TrainingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingViewHolder holder, int position) {
        // 绑定数据到视图
        TrainingPlan plan = plans.get(position);
        holder.dateTextView.setText(plan.getTrainingDate());
        holder.distanceTextView.setText("Distance: " + plan.getDistance() + " km");
        holder.paceTextView.setText("Pace: " + plan.getPace());
        holder.typeTextView.setText("Type: " + plan.getTrainingType());
        holder.durationTextView.setText("Estimated Duration: " + plan.getEstimatedDuration());
        holder.notesTextView.setText("Notes: " + plan.getNotes());

        holder.itemView.setOnClickListener(v -> {
            // 调用 MainActivity 的方法来完成跳转
            if (fragment.getActivity() instanceof MainActivity) {
                ((MainActivity) fragment.getActivity()).navigateToHomeAndOpenNewRun();
            }
        });
    }

    @Override
    public int getItemCount() {
        return plans != null ? plans.size() : 0;
    }

    public void updateData(List<TrainingPlan> newPlans) {
        this.plans = newPlans;
        notifyDataSetChanged();
    }

    static class TrainingViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        TextView distanceTextView;
        TextView paceTextView;
        TextView typeTextView;
        TextView durationTextView;
        TextView notesTextView;

        public TrainingViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.text_date);
            distanceTextView = itemView.findViewById(R.id.text_distance);
            paceTextView = itemView.findViewById(R.id.text_pace);
            typeTextView = itemView.findViewById(R.id.text_type);
            durationTextView = itemView.findViewById(R.id.text_duration);
            notesTextView = itemView.findViewById(R.id.text_notes);
        }
    }
}