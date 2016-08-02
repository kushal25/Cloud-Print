package com.cloudprint;

import android.content.Context;


public interface DocService {
    public void postDocument(User user, Document doc, Context context);
    public void delete(User user, Document doc, Context context);
}
