package com.example.schoolservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import com.example.schoolservice.model.SchoolModel;
import com.example.schoolservice.repo.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class SchoolServiceApplication implements CommandLineRunner {

    @Autowired
    SchoolRepository schoolRepository;

    public static void main(String[] args) {
        SpringApplication.run(SchoolServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SchoolModel model1 = new SchoolModel("AtaturkLisesi" , "Ankara/Cankaya");;
        schoolRepository.save(model1);

        SchoolModel model2 = new SchoolModel("AnadoluLisesi" , "Ankara/Ulus");;
        schoolRepository.save(model2);

        SchoolModel model3 = new SchoolModel("FenLisesi" , "Bursa/Osmangazi");;
        schoolRepository.save(model3);

        SchoolModel model4 = new SchoolModel("MeslekLisesi" , "Istanbul/Esen");;
        schoolRepository.save(model4);
    }

}