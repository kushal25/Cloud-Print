package com.cloudprint.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudprint.CloudPrint;
import com.cloudprint.Markers;
import com.cloudprint.R;

import java.util.ArrayList;
import java.util.List;

public class MapListViewAdapter extends ArrayAdapter<Markers> {
    private Activity context;
    private ArrayList<Markers> markers = new ArrayList<>();
    ViewHolder holder;
    Markers m;
    private int row;

    static class ViewHolder {
        public TextView title;
        public TextView description;
        public TextView latitude;
        public TextView longitude;
    }

    public MapListViewAdapter(Activity act, int row,
                           ArrayList<Markers> items) {
        super(act, row, items);
        this.context = act;
        this.row = row;
        this.markers = items;
    }

    public void concatList(ArrayList<Markers> newMarkers) {
        this.markers = newMarkers;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.markers.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        try {

            m = this.markers.get(position);
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.maplist, null);

                holder = new ViewHolder();
                holder.title = (TextView) rowView.findViewById(R.id.title);
                holder.description = (TextView) rowView.findViewById(R.id.description);
                holder.latitude = (TextView) rowView.findViewById(R.id.latitude);
                holder.longitude = (TextView) rowView.findViewById(R.id.longitude);

                rowView.setTag(holder);
            } else {
                holder = (ViewHolder) rowView.getTag();
            }
            holder.title.setText(m.getTitle());
            holder.description.setText(m.getDescription());
            holder.latitude.setText(m.getLatitude()+"");
            holder.longitude.setText(m.getLongitude()+"");
            return rowView;
        } catch (Exception e) {
            return rowView;
        }
    }

    public void destroy() {
        this.context = null;
        this.holder = null;
    }
}
