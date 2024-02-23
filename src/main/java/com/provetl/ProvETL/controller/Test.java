package com.provetl.ProvETL.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {

    @GetMapping
    public String test() {
        return "Test";
    }

    @PostMapping
    public ResponseEntity<?> testJsonDynamically(@RequestBody Map<String, Object> tableData) {
        System.out.println(tableData);

        return ResponseEntity.ok("Ok");
    }

}
