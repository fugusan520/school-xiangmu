package com.example.api;

import com.example.api.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Hashtable;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
public class ApiApplication {
    public static void main(String[] args) {
        System.out.println("==========冲锋===========");
        SpringApplication.run(ApiApplication.class, args);
    }

}
