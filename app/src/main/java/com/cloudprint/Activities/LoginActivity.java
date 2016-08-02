package com.cloudprint.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudprint.CloudPrint;
import com.cloudprint.R;
import com.cloudprint.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LoginActivity extends Activity {

    private static final String url = "https://kushal.firebaseio.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.action_bar);
        TextView tv = (TextView) findViewById(R.id.Title);
        tv.setText(R.string.login);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_login);
        getIntent();
        final Boolean isInternetPresent = CloudPrint.isNetworkOK(LoginActivity.this);

        if (!isInternetPresent) {
            CloudPrint.showAlertDialog(LoginActivity.this, "No Internet Connection",
                    "Press Ok to redirect to Network Connections");

        }
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText pass = (EditText) findViewById(R.id.pass);
        final EditText mobno = (EditText) findViewById(R.id.mobno);

        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!isInternetPresent) {
                    CloudPrint.showAlertDialog(LoginActivity.this, "No Internet Connection",
                            "Press Ok to redirect to Network Connections");

                }
                else {

                    if(mobno.length()==0 || email.length()==0 || pass.length()==0)
                    {
                        Toast.makeText(getApplicationContext(), "Fields Cannot Be Empty", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        final String nu = email.getText().toString();
                        final String pa = pass.getText().toString();
                        final String mob = mobno.getText().toString();
                        Firebase fb = new Firebase(url).child("users");
                        fb.child(mob).addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {

                                if(nu.equals(snapshot.child("email").getValue()) && pa.equals(snapshot.child("password").getValue()))
                                {

                                    final ProgressDialog pd = ProgressDialog.show(LoginActivity.this, "Please Wait", "Loading Map Markers....");
                                    pd.setCancelable(false);
                                    new Thread(new Runnable(){
                                        public void run(){
                                            try{
                                                Thread.sleep(4000);
                                            }catch(Exception e){}
                                            pd.dismiss();
                                        }
                                    }).start();

                                    String name = snapshot.child("name").getValue().toString();
                                    Intent user_con = new Intent(LoginActivity.this,MapActivity.class);
                                    user_con.putExtra("name", name);
                                    user_con.putExtra("email", nu);
                                    user_con.putExtra("mobno", mob);
                                    Bundle bndlanimation =
                                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation1,R.anim.animation2).toBundle();
                                    startActivity(user_con, bndlanimation);

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(FirebaseError error) {
                                // TODO Auto-generated method stub
                                Toast.makeText(getApplicationContext(), "Failed. Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent back = new Intent(LoginActivity.this,MainActivity.class);
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();
        startActivity(back, bndlanimation);
    }

}
