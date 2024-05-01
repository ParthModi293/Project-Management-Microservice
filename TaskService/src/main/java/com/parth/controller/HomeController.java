package com.parth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


    @GetMapping("/task")
    public ResponseEntity<String> test(){

        return  new ResponseEntity<>("welcome to task", HttpStatus.OK);
    }

}
