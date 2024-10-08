package com.example.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;


@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class ApiGatewayApplication implements CommandLineRunner {

    @Value("${application.message}")
    String appMesage ;

    @Value("${spring.application.name}")
    String appName;

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Override
    public void run(String... args)  {
        System.out.println(appMesage + "-" + appName);
    }
}
