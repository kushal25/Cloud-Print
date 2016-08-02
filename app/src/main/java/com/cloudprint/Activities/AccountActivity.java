package com.cloudprint.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.cloudprint.R;

public class AccountActivity extends Activity {

    private  String name, email, mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        name = getIntent().getExtras().getString("name");
        email = getIntent().getExtras().getString("email");
        mobile = getIntent().getExtras().getString("mobno");
        TextView n = (TextView) findViewById(R.id.fullName);
        TextView e = (TextView) findViewById(R.id.emailAddr);
        TextView m = (TextView) findViewById(R.id.mobileNum);

        n.setText(n.getText() + name);
        e.setText(e.getText() + email);
        m.setText(m.getText() + mobile);
    }
}
