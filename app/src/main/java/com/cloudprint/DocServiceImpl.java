package com.cloudprint;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Chavi on 11/8/15.
 */
public class DocServiceImpl implements DocService{

    public DocServiceImpl(){}

    @Override
    public void postDocument(final User user, final Document doc, final Context context){
        JSONObject docParams = new JSONObject();
        JSONObject userParams = new JSONObject();
        JSONObject finalParams = new JSONObject();


        try {
            docParams.put("docName", doc.getName());
            docParams.put("doc_url", doc.getUrl());
            userParams.put("_id", user.get_id());
            userParams.put("newDoc", docParams);
            finalParams.put("user", userParams);
        }catch(Exception e){
            Log.e("JsonParam", "Unable to construct JSON param object");
        }


        String jsonString = finalParams.toString().replace("\\", "");
        Log.i("jsonString", jsonString);

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonString);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        UserClient.addHeader("X-Auth-Token", user.getToken());
        UserClient.post(context, "api/users/doc/", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseString) {
                //super.onSuccess(statusCode, headers, responseString);
                String str;
                str = new String(responseString);
                Log.i("Success", str);
                try {
                    JSONObject jsonObj = new JSONObject(str);
                    //docs = jsonObj.getString("document");
                    Log.i("jsonString", jsonObj.toString());
                    JSONArray array = jsonObj.getJSONArray("document");
                    Log.i("json", "Length is " +array.length());
                    for(int i = 0 ; i < array.length() ; i++){
                        if(doc.getUrl().equals(array.getJSONObject(i).getString("doc_url").replace("\\", ""))){
                            doc.set_id(array.getJSONObject(i).getString("_id"));
                        }
                    }
                    Log.i("DocumentID", doc.get_id());
                } catch (Exception e) {
                    Log.e("Json", "Error");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Log.e("Post", "failure: " + new String(responseBody));
                Log.e("Post", "failurecode: " + statusCode);

                String str = new String(responseBody);
                String message="";

                try {
                    JSONObject resp = new JSONObject(str);
                    message = resp.getString("error");
                }catch(Exception e){
                    Log.e("Failure Response", "Error");
                }
                if(statusCode == 400){
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

                //super.onFailure(statusCode, headers, responseBody, throwable);
            }


        });

    }

    @Override
    public void delete(final User user, final Document doc, Context context){

    }

}
