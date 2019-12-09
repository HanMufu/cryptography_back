package com.rutgers.cryptography.service;

/**
 * Created by Qin Yuxin on 2019/12/8 12:53
 */


public interface AESService {

    String generateAESKey();

    String encryptAES(String plainText, String pubKey);

}
