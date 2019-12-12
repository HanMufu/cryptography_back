package com.rutgers.cryptography.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.rutgers.cryptography.service.SignService;
import org.springframework.stereotype.Service;
import com.rutgers.cryptography.bean.*;

@Service
public class SignServiceImpl implements SignService {
    private static RSAKeyPrivate privatekey;
    private static RSAKeyPublic publickey;

    @Override
    public String generateSignKey() {
        JSONObject responseData = new JSONObject();
        JSONObject data = new JSONObject();
        RSAKeyPrivate prk = new RSAKeyPrivate();
        RSAKeyPublic puk = new RSAKeyPublic();
        KeyGenerator k = new KeyGenerator(prk, puk);
        String publicKey = puk.getPublicKey();
        String privateKey = prk.getPrivateKey();
        privatekey = prk;
        publickey = puk;
        System.out.println("saved alice's keys");
        //  ----------------------------------------------------------------------------
        System.out.println("public key: " + publicKey);
        System.out.println("private key: " + privateKey);
        data.put("pub_key", publicKey);
        data.put("pri_key", privateKey);
        responseData.put("key_data", data);
        return responseData.toJSONString();
    }

    @Override
    public String encryptSign(String plainText) {
        String sign = privatekey.Sign(plainText);
        System.out.println("The sign is: " + sign);
        String veri_text = publickey.verifySign(sign);
        System.out.println("verify sign text is :" + veri_text);
        // ----------------------------------------------------------------------------
        JSONObject responseData = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("cipher_text", sign);
        data.put("decrypt_text", veri_text);
        responseData.put("text_data", data);;
        return responseData.toJSONString();
    }

}
