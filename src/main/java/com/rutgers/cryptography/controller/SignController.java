package com.rutgers.cryptography.controller;

import com.rutgers.cryptography.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Service;

import java.util.Map;

@RestController
@RequestMapping(value ="/sign")
public class SignController {

    @Autowired
    private SignService signService;

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public String generateSignKey() {
        return signService.generateSignKey();
    }

    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public String encryptSign(@RequestBody Map<String, Object> params) {
        String plainText = params.get("plain_text").toString();
        return signService.encryptSign(plainText);
    }
}
