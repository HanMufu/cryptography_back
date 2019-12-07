package com.rutgers.cryptography.service;

import org.springframework.stereotype.Service;

/**
 * Created by Qin Yuxin on 2019/12/7 21:40
 */


public interface RSAService {

    public String generateRSAKey();

    public String encryptRSA(String plainText, String pubKey);

}
