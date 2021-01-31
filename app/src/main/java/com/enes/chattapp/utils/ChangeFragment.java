package com.enes.chattapp.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.contentcapture.ContentCaptureCondition;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.enes.chattapp.R;

public class ChangeFragment {

    public  Context context;

    public ChangeFragment(Context context) {
        this.context = context;
    }

    public void change(Fragment fragment){
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout,fragment,"fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
    public  void changeWithparemeter(Fragment fragment,String userid){

        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        fragment.setArguments(bundle);


        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout,fragment,"fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }

}
