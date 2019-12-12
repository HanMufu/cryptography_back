package com.rutgers.cryptography.service;

public interface RSAService {

    String generateRSAKey(String who);

    String encryptRSA(String plainText);

}
