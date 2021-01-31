package com.enes.chattapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class getData {
    public static  String getData(){



            String pattern = "dd-M-yyyy hh:mm:ss ";
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());

            return date;


    }

}
