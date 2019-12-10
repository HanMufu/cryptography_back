package com.rutgers.cryptography.controller;

import com.rutgers.cryptography.service.AESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value ="/aes")
public class AESController {

    @Autowired
    private AESService aesService;

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public String generateAESKey() {
        return aesService.generateAESKey();
    }

    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public String encryptRSA(@RequestBody Map<String, Object> params) {
        String plainText = params.get("plain_text").toString();
        String pubKey = params.get("pub_key").toString();
        return aesService.encryptAES(plainText, pubKey);
    }
}
