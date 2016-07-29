package com.cloudprint.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cloudprint.Adapters.MapListViewAdapter;
import com.cloudprint.CloudPrint;
import com.cloudprint.Markers;
import com.cloudprint.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MapListView extends AppCompatActivity {

    private ListView mapList;
    private MapListViewAdapter mlvAdapter;
    private ArrayList<Markers> ms = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list_view);
        initView();
        populateData();
        mlvAdapter = new MapListViewAdapter(MapListView.this, R.layout.activity_map_list_view, ms);
        mapList.setAdapter(mlvAdapter);
        //hideProgressLoader();
    }

    private void initView()
    {
        mapList = (ListView) findViewById(R.id.mapList);
    }

    public void populateData() {

                final List<String[]> data = CloudPrint.readCsv(getApplicationContext());

                for (int i = 0; i < 20; i++) {
                    Markers marker = new Markers();
                    marker.setLatitude(Double.parseDouble(data.get(i)[26]));
                    marker.setLongitude(Double.parseDouble(data.get(i)[25]));
                    marker.setTitle(data.get(i)[4]);
                    marker.setDescription(data.get(i)[5] + "," + data.get(i)[6]);
                    ms.add(marker);
                    Log.d("Cloud",ms.get(i).getTitle());
                }
    }
}
