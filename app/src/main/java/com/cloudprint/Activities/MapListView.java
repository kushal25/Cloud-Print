package com.cloudprint.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
    private ArrayList<Markers> msList = new ArrayList<>();
    private MapListViewAdapter mlvAdapter;
    private ArrayList<Markers> ms = new ArrayList<>();
    private  Markers marker = new Markers();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list_view);
        initView();
        msList = populateData();


        mlvAdapter = new MapListViewAdapter(this,msList);
        mapList.setAdapter(mlvAdapter);
//        MapListView.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mlvAdapter.concatList(msList);
//            }
//        });
    }

    private void initView()
    {
        mapList = (ListView) findViewById(R.id.mapList);
    }

    public ArrayList<Markers> populateData() {

        final List<String[]> data = CloudPrint.readCsv(getApplicationContext());
        MapListView.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 20; i++) {
                    marker.setLatitude(Double.parseDouble(data.get(i)[26]));
                    marker.setLongitude(Double.parseDouble(data.get(i)[25]));
                    marker.setTitle(data.get(i)[4]);
                    marker.setDescription(data.get(i)[5]+","+data.get(i)[6]);
                    //Log.d("Cloud",marker.getTitle());
                    ms.add(marker);
                }
                
            }
        });


        return ms;

    }
}
