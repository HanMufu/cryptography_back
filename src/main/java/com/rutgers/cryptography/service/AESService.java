package com.rutgers.cryptography.service;

public interface AESService {

    String generateAESKey();

    String encryptAES(String plainText, String pubKey);

}
