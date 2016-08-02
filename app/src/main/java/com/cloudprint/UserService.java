package com.cloudprint;


import android.content.Context;


import org.json.JSONException;

public interface UserService {
    public void login(User user, Context context);
    public void register(User user, Context context);
}

