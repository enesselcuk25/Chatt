package com.enes.chattapp.utils;

import android.content.Context;
import android.widget.Toast;

public class showToastMessage {

    private Context context;

    public showToastMessage(Context context){
        this.context = context;
    }

    public void showtext(String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }
}
