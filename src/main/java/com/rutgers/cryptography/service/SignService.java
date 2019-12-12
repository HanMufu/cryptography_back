package com.rutgers.cryptography.service;

public interface SignService {

    String generateSignKey();

    String encryptSign(String plainText);

}
