package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@SpringBootApplication
public class DemoApplicationTests {

	
	public static void main(String[] args) {
        SpringApplication.run(DemoApplicationTests.class, args);
    }
	
}
