package com.cloudprint;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CloudPrint {

    private static Context context;
    public static Typeface fontAwesome = null;
    private  static String CSV_PATH = "sample.csv";
    public static void appInit(Context cx) {
        P.read(cx);
        context = cx;
    }

    public static void runOnBackground(final Context cx, final Runnable r) {
        new Thread() {
            @Override
            public void run() {
                try {
                    r.run();
                } catch (Exception e) {
                    //L.fe(cx, Event.EXCEPTION, e);
                    Log.d("Error", e.toString());
                }
            }
        }.start();
    }

    public static void showToast(String str)
    {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBar(View view, String str)
    {
        final Snackbar sb = Snackbar.make(view, str , Snackbar.LENGTH_LONG);

        sb.setAction("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sb.dismiss();
                    }
                });

        sb.show();
    }

    public static List<String[]> readCsv(Context context) {
        List<String[]> questionList = new ArrayList<String[]>();
        AssetManager assetManager = context.getResources().getAssets();

        try {
            InputStream csvStream = assetManager.open(CSV_PATH);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            CSVReader csvReader = new CSVReader(csvStreamReader);
            String[] line;

            // throw away the header
            csvReader.readNext();

            while ((line = csvReader.readNext()) != null) {
                questionList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questionList;
    }

    public static Boolean isGPSEnabled()
    {
        final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );

        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNetworkOK(Context cx) {
        try {
            ConnectivityManager cm = (ConnectivityManager) cx.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null == cm) {
                return false;
            }
            NetworkInfo ni;
            ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != ni && ni.isConnectedOrConnecting()) return true;
            ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (null != ni && ni.isConnectedOrConnecting()) return true;
            ni = cm.getActiveNetworkInfo();
            if (null != ni && ni.isConnectedOrConnecting()) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static final class P {

        public static void read(Context cx)
        {
            try {
                if (null == fontAwesome) {
                    fontAwesome = Typeface.createFromAsset(cx.getAssets(), "fontawesome-webfont.ttf");
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
