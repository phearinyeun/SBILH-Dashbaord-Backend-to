package com.sbilhbank.insur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class App extends SpringBootServletInitializer{
    public static void main(String[] args) throws Exception{
        SpringApplication.run(App.class, args);
    }
}
