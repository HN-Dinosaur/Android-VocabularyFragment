package com.example.myandroidexperiment2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myandroidexperiment2.R;

import java.util.List;

public class Adapter extends BaseAdapter {

    List<String> wordTitles;
    private LayoutInflater layoutInflater;
    private int selectionPosition = -1;
    Adapter(List<String> wordTitles, Context context){
        this.wordTitles = wordTitles;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return wordTitles.size();
    }

    @Override
    public String getItem(int i) {
        return wordTitles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.fragment_list_item, null);
            viewHolder.tv = convertView.findViewById(R.id.list_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(wordTitles.get(position));

        if (selectionPosition == position) {
            viewHolder.tv.setBackgroundColor(Color.RED);
        } else {
            viewHolder.tv.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    public void setSelectedPosition(int position) {
        this.selectionPosition = position;
    }

    class ViewHolder {
        TextView tv;
    }
}
