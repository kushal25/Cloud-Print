package com.cloudprint.Activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferProgress;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.cloudprint.CloudPrint;
import com.cloudprint.DocServiceImpl;
import com.cloudprint.Document;
import com.cloudprint.R;
import com.cloudprint.User;
import com.cloudprint.UserServiceImpl;
import com.dropbox.chooser.android.DbxChooser;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.net.URL;

public class PrintActivity extends AppCompatActivity {

    private Button upload;
    public static final String BASE_FILE_URL = "https://s3.amazonaws.com/clowdprint/";
    private Button print;
    Context context;
    public static final String BUCKET_NAME = "cloudprint";
    private static final int REQUEST_CODE = 6384;
    static final int DBX_CHOOSER_REQUEST = 0;
    String upLoadServerUri = null;
    int serverResponseCode = 0;
    private User user1;
    private Document doc;
    private DocServiceImpl docService;
    private UserServiceImpl userService;
    private String uploadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        initViews();
        user1 = new User();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser();
            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printFile(uploadUrl);
            }
        });
    }

    private void initViews()
    {
        upload = (Button) findViewById(R.id.upload);
        print = (Button) findViewById(R.id.print);
        print.setVisibility(View.GONE);
    }

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);

        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(data);
                Log.d("main", "Link to selected file: " + result.getLink());

                // Handle the result
            } else {
                // Failed or was cancelled by the user.
            }
        } else if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    // Get the URI of the selected file
                    final Uri uri = data.getData();
                    Log.i("Cloud", "Uri = " + uri.toString());
                    try {
                        // Get the file path from the URI
                        final String path = FileUtils.getPath(this, uri);
                        Toast.makeText(PrintActivity.this,
                                "File Selected: " + path, Toast.LENGTH_LONG).show();

                        upLoadServerUri = "http://www.chavisjsu.com/UploadToServer.php";

                        new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(PrintActivity.this, "uploading started.....", Toast.LENGTH_LONG).show();
                                        uploadFile(path);
                                    }
                                });

                            }
                        }).start();

                    } catch (Exception e) {
                        Log.e("FileSelectorTestAcivity", "File select error", e);
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void printFile(String url)
    {
        try {
            Uri uri = Uri.parse("googlechrome://navigate?url=" + url);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            // Chrome is probably not installed
        }


    }

    public void uploadFile(String sourceFileUri) {

        final String fileName = sourceFileUri;
        File sourceFile = new File(sourceFileUri);
        //Looper.prepare();
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :"
                    +fileName);
        }
        else
        {
            // Initialize the Amazon Cognito credentials provider
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:0b413e9f-7a1b-44b7-adac-9ee2d229da56", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            //Toast.makeText(LoggedUser.this, "CRED DONE", Toast.LENGTH_LONG).show();
            Log.i("uploadFile", "CRED DONE");

            TransferManager transferManager = new TransferManager(credentialsProvider);

            Upload upload = transferManager.upload(BUCKET_NAME, sourceFile.getName(), sourceFile);
            AmazonS3Client client = new AmazonS3Client(credentialsProvider);

            URL fileURL = client.getUrl(BUCKET_NAME, sourceFile.getName());

            Log.i("uploadFile", sourceFile.getName());
            Log.i("uploadFile", BASE_FILE_URL+sourceFile.getName());
            Log.i("uploadFile URL", fileURL.toString());
            //Log.i("uploadFile", user1.getFirstName());


            while (!upload.isDone()){
                //Show a progress bar...
                TransferProgress transferred = upload.getProgress();
                Toast.makeText(PrintActivity.this, "Uploading... ", Toast.LENGTH_LONG).show();
                Log.i("Percentage", "" +transferred.getPercentTransferred());
            }

            if(upload.isDone()){
                Toast.makeText(PrintActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                doc = new Document();
                doc.setName(sourceFile.getName());
                doc.setUrl(fileURL.toString());

                docService = new DocServiceImpl();
                user1.setToken("Sample");
                docService.postDocument(user1, doc, context);

                Log.i("Document", "ID is " + user1.get_id());
                Log.i("Document", "DocID is " + doc.get_id());
                Log.i("Document", "Name is " + doc.getName());
                Log.i("Document", "URL is " + doc.getUrl());

                uploadUrl= doc.getUrl();
                if(uploadUrl.length()>0)
                {
                    print.setVisibility(View.VISIBLE);
                }



            }



        } // End else block
    }

}
