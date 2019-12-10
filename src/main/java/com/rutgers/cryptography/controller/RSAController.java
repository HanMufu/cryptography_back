package com.rutgers.cryptography.controller;

import com.rutgers.cryptography.service.RSAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Service;

import java.util.Map;

@RestController
@RequestMapping(value ="/rsa")
public class RSAController {

    @Autowired
    private RSAService rsaService;

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public String generateRSAKey() {
        return rsaService.generateRSAKey();
    }

    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public String encryptRSA(@RequestBody Map<String, Object> params) {
        String plainText = params.get("plain_text").toString();
        String pubKey = params.get("pub_key").toString();
        return rsaService.encryptRSA(plainText, pubKey);
    }
}
