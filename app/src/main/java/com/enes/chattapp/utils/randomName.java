package com.enes.chattapp.utils;

import java.util.Random;

public class randomName {

    public static  String getSaltString(){
        String SALTCHARS = "ABCDEFGHIJKLMNOPRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18){
            int index = (int) (rnd.nextFloat() *  SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltstr = salt.toString();
        return saltstr;
    }
}
