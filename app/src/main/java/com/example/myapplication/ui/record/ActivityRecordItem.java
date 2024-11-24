package com.example.myapplication.ui.record;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.RunningRecord;

import java.util.List;

public class ActivityRecordItem extends BaseAdapter {

    private Context context;
    private List<RunningRecord> items;

    // Constructor to pass context and data
    public ActivityRecordItem(Context context, List<RunningRecord> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        // Get the data item for this position
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.record_list_item, parent, false);
        }
        TextView recordTitle = view.findViewById(R.id.recordTitle);
        TextView recordDate = view.findViewById(R.id.recordTime);
        TextView recordDistance = view.findViewById(R.id.recordDistance);

        recordTitle.setText(items.get(i).getPlace() + " Running");
        recordDate.setText(items.get(i).getTimestamp());
        recordDistance.setText(items.get(i).getDistance() + " km");
        
        return view;
    }

}
