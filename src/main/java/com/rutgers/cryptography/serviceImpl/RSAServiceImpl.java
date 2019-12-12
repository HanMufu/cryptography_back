package com.rutgers.cryptography.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.rutgers.cryptography.service.RSAService;
import org.springframework.stereotype.Service;
import com.rutgers.cryptography.bean.*;

@Service
public class RSAServiceImpl implements RSAService{
    @Override
    public String generateRSAKey(String who){
        JSONObject responseData = new JSONObject();
        JSONObject data = new JSONObject();
        RSAKeyPrivate prk = new RSAKeyPrivate();
        RSAKeyPublic puk = new RSAKeyPublic();
        KeyGenerator k = new KeyGenerator(prk, puk);
        String publicKey = puk.getPublicKey();
        String privateKey = prk.getPrivateKey();
        System.out.println(who);
        if(who.equals("A")) {
            KeyMemory.privatekey_alice = prk;
            KeyMemory.publickey_alice = puk;
            System.out.println("saved alice's keys");
        }
        //  ----------------------------------------------------------------------------
        data.put("pub_key", publicKey);
        data.put("pri_key", privateKey);
        responseData.put("key_data", data);
        return responseData.toJSONString();
    }

    @Override
    public String encryptRSA(String plainText){
        JSONObject responseData = new JSONObject();
        JSONObject data = new JSONObject();
        //  ----------------need to be modified with [rsa encrypt] code----------------
//        String cipherText = "hmf's cipher text!";  // TODO
//        String decryptText = "I want get decrypt text!";  // TODO
        String cipherText = KeyMemory.publickey_alice.encrypt(plainText);
        System.out.println(cipherText);
        String decryptText = KeyMemory.privatekey_alice.getDecryptedText(cipherText);
        System.out.println("decrypted text is :" + decryptText);
        //  ----------------------------------------------------------------------------
        data.put("cipher_text", cipherText);  // "pub_key" have to be same with frontend's
        data.put("decrypt_text", decryptText);
        responseData.put("text_data", data);
        return responseData.toJSONString();
    }
}
