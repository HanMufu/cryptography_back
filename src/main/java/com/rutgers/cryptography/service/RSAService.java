package com.rutgers.cryptography.service;


/**
 * Created by Qin Yuxin on 2019/12/7 21:40
 */


public interface RSAService {

    String generateRSAKey();

    String encryptRSA(String plainText, String pubKey);

}
