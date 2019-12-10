package com.rutgers.cryptography.service;

public interface RSAService {

    String generateRSAKey();

    String encryptRSA(String plainText, String pubKey);

}
