package com.rutgers.cryptography.bean;

import java.util.UUID;

public class KeyGenerator {
    public static String getAESRandomKey(){
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        //String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        String key = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18);
        return key;
    }
}
