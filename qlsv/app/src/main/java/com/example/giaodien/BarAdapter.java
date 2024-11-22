package com.example.giaodien;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BarAdapter extends BaseAdapter {
    private Activity context;
    private int rerouse;
    private List<Bar> arrayList;

    public BarAdapter(Activity context, int rerouse, List<Bar> arrayList) {
        this.context = context;
        this.rerouse = rerouse;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(rerouse,null);

        ImageView imageView = view.findViewById(R.id.imgIcon);
        TextView textView = view.findViewById(R.id.textViewBar);
        Bar bar = arrayList.get(i);
        imageView.setImageResource(bar.getImg());
        textView.setText(bar.getName());
        return view;
    }
}

