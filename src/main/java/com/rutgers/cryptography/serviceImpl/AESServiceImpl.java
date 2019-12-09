package com.rutgers.cryptography.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.rutgers.cryptography.service.AESService;
import org.springframework.stereotype.Service;
import com.rutgers.cryptography.bean.AES;

/**
 * Created by Qin Yuxin on 2019/12/8 12:59
 */

@Service
public class AESServiceImpl implements AESService{
    @Override
    public String generateAESKey(){
        return "qwe";
    }

    @Override
    public String encryptAES(String plainText, String pubKey){
        // Key must be 16-bit, while text must longer than 16-bit
        AES aes = new AES();
        JSONObject responseData = new JSONObject();

        String cipherText = aes.encrypt(plainText, pubKey);
        String decryptText = aes.decrypt(cipherText, pubKey);

        responseData.put("cipher_text", cipherText);  // "pub_key" have to be same with frontend's
        responseData.put("decrypt_text", decryptText);
        return responseData.toJSONString();

    }

}
