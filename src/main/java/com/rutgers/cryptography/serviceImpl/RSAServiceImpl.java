package com.rutgers.cryptography.serviceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rutgers.cryptography.service.RSAService;
import org.springframework.stereotype.Service;

@Service
public class RSAServiceImpl implements RSAService{
    @Override
    public String generateRSAKey(){
        JSONObject responseData = new JSONObject();
        JSONObject data = new JSONObject();
        //  ----------------need to be modified with [rsa generate] code----------------
        String publicKey = "hmf's public key!";  // TODO
        String privateKey = "I want get qyx's public key!";  // TODO
        boolean ifSuccess = false;  // to get if your rsa keys generate successfully, need to be changed.
        //  ----------------------------------------------------------------------------

        data.put("pub_key", publicKey);  // "pub_key" have to be same with frontend's
        data.put("pri_key", privateKey);
        responseData.put("key_data", data);
        if (ifSuccess) {
            responseData.put("statusCode", 500);  // error code is 500
        } else {
            responseData.put("statusCode", 200);
        }
        return responseData.toJSONString();
    }

    @Override
    public String encryptRSA(String plainText, String pubKey){
        JSONObject responseData = new JSONObject();
        JSONObject data = new JSONObject();
        //  ----------------need to be modified with [rsa encrypt] code----------------
        String cipherText = "hmf's cipher text!";  // TODO
        String decryptText = "I want get decrypt text!";  // TODO
        boolean ifSuccess = false;  // to get if your rsa keys generate successfully, need to be changed.
        //  ----------------------------------------------------------------------------

        data.put("cipher_text", cipherText);  // "pub_key" have to be same with frontend's
        data.put("decrypt_text", decryptText);
        responseData.put("text_data", data);
        if (ifSuccess) {
            responseData.put("statusCode", 500);  // error code is 500
        } else {
            responseData.put("statusCode", 200);
        }
        return responseData.toJSONString();
    }
}
