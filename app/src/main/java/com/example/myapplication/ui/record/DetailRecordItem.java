package com.example.myapplication.ui.record;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;
import java.util.Map;


public class DetailRecordItem extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> items;

    // Constructor to pass context and data
    public DetailRecordItem(Context context, List<Map<String, String>> items) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.recorddetail_list_item, viewGroup, false);
        }
        TextView lap = view.findViewById(R.id.detail_lap);
        TextView time = view.findViewById(R.id.detail_time);
        TextView distance = view.findViewById(R.id.detail_distance);
        TextView pace = view.findViewById(R.id.detail_pace);

        lap.setText(items.get(i).get("lap"));
        time.setText(items.get(i).get("time"));
        distance.setText(items.get(i).get("distance"));
        pace.setText(items.get(i).get("pace"));

        return view;
    }


}
