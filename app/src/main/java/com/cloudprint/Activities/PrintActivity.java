package com.cloudprint.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cloudprint.CloudPrint;
import com.cloudprint.R;

public class PrintActivity extends AppCompatActivity {

    private Button upload;
    private Button print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        initViews();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser();
            }
        });
    }

    private void initViews()
    {
        upload = (Button) findViewById(R.id.upload);
        print = (Button) findViewById(R.id.print);
    }

    private void showChooser() {
       CloudPrint.showToast("Upload in Progress");
    }
}
