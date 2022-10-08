package com.example.schoolservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(value = "/school/test")
public class TestController {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    RestTemplate restTemplate;

    @GetMapping(value = "/info")
    public ResponseEntity<String> whoAmIService(){
        StringBuilder appInfo = new StringBuilder();
        appInfo.append(applicationContext.getId() + "-") ;
        appInfo.append(applicationContext.getApplicationName()+ "-");
        appInfo.append(applicationContext.getStartupDate());
        return new ResponseEntity<String>(String.valueOf(appInfo), HttpStatus.OK) ;
    }

}