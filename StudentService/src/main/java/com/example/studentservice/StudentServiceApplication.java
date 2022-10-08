package com.example.studentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import com.example.studentservice.model.StudentModel;
import com.example.studentservice.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class StudentServiceApplication implements CommandLineRunner {

    @Autowired
    StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(StudentServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        StudentModel model1 = new StudentModel("Ahmet" , 19 , "AtaturkLisesi") ;
        studentRepository.save(model1);

        StudentModel model2 = new StudentModel("Mehmet" , 19 ,  "AnadoluLisesi") ;
        studentRepository.save(model2);

        StudentModel model3 = new StudentModel("Ayşe" , 19 , "AnadoluLisesi") ;
        studentRepository.save(model3);

        StudentModel model4 = new StudentModel("Elif" , 20 , "AnadoluLisesi") ;
        studentRepository.save(model4);

        StudentModel model5 = new StudentModel("Huseyin" , 17 , "FenLisesi") ;
        studentRepository.save(model5);

        StudentModel model6 = new StudentModel("Sadettin" , 17 , "MeslekLisesi") ;
        studentRepository.save(model6);
    }
}