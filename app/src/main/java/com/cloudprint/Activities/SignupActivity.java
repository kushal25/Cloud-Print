package com.cloudprint.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudprint.CloudPrint;
import com.cloudprint.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class SignupActivity extends Activity {

    private static final String url = "https://kushal.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.action_bar);
        TextView tv = (TextView) findViewById(R.id.Title);
        tv.setText(R.string.signup);

        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_signup);
        getIntent();
        final Boolean isInternetPresent = CloudPrint.isNetworkOK(SignupActivity.this);

        if (!isInternetPresent) {
            CloudPrint.showAlertDialog(SignupActivity.this, "No Internet Connection",
                    "Press Ok to redirect to Network Connectionss");

        }


        final EditText name = (EditText) findViewById(R.id.name);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText mobno = (EditText) findViewById(R.id.mobno);
        final EditText pass = (EditText) findViewById(R.id.pass);
        final EditText repass = (EditText) findViewById(R.id.repass);
        Button signup = (Button) findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!isInternetPresent) {
                    CloudPrint.showAlertDialog(SignupActivity.this, "No Internet Connection",
                            "You don't have internet connection.");
                }
                if(name.length()==0 ||email.length()==0 || mobno.length()==0 || pass.length()==0 || repass.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Fields Cannot Be Empty", Toast.LENGTH_LONG).show();
                }
                else if(!pass.getText().toString().equals(repass.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Password does not Match", Toast.LENGTH_LONG).show();
                }

                else
                {
                    String emai = email.getText().toString();
                    String nam = name.getText().toString();
                    String mob = mobno.getText().toString();
                    final String pa = pass.getText().toString();
                    Firebase fb = new Firebase(url);

                    fb.child("users").child(mob).child("name").setValue(nam,new Firebase.CompletionListener() {

                        @Override
                        public void onComplete(FirebaseError error, Firebase firebase) {
                            if(error!=null)
                            {
                                Toast.makeText(getApplicationContext(), "Retry Again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    fb.child("users").child(mob).child("mobno").setValue(mob,new Firebase.CompletionListener() {

                        @Override
                        public void onComplete(FirebaseError error, Firebase firebase) {
                            if(error!=null)
                            {
                                Toast.makeText(getApplicationContext(), "Retry Again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    fb.child("users").child(mob).child("email").setValue(emai, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError error, Firebase firebase) {
                            if(error!=null)
                            {
                                Toast.makeText(getApplicationContext(), "Retry Again", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    fb.child("users").child(mob).child("password").setValue(pa, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError error, Firebase firebase) {
                            if(error!=null)
                            {
                                Toast.makeText(getApplicationContext(), "Retry Again", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    final ProgressDialog pd = ProgressDialog.show(SignupActivity.this, "Please Wait", "Loading....");
                    pd.setCancelable(false);
                    new Thread(new Runnable(){
                        public void run(){
                            try{
                                Thread.sleep(4000);
                            }catch(Exception e){}
                            pd.dismiss();
                        }
                    }).start();
                    Intent user_con = new Intent(SignupActivity.this,MapActivity.class);
                    user_con.putExtra("name", nam);
                    user_con.putExtra("email", emai);
                    user_con.putExtra("mobno", mob);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation1,R.anim.animation2).toBundle();
                    startActivity(user_con, bndlanimation);
                }
            }

        });
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent back = new Intent(SignupActivity.this,MainActivity.class);
        Bundle bndlanimation =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();
        startActivity(back, bndlanimation);
    }
}

