package com.cleansolution.illkam;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {
//    health checkdd
    @GetMapping("/api/v1/")
    public String test(){
        System.out.println("asdfasdf");
        return "ok";
    }

    @GetMapping("/")
    public String healthCheck(){
        System.out.println("asdfasdf");
        return "ok";
    }


}
