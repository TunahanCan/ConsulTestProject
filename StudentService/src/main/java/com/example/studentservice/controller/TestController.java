package com.example.studentservice.controller;

import com.example.studentservice.model.StudentModel;
import com.example.studentservice.repo.StudentRepository;
import com.example.studentservice.security.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/student/test")
public class TestController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    StudentRepository studentRepository;

    @GetMapping(value = "/info")
    public ResponseEntity<String> whoAmIService() {
        StringBuilder appInfo = new StringBuilder();
        appInfo.append(applicationContext.getId() + "-");
        appInfo.append(applicationContext.getApplicationName() + "-");
        appInfo.append(applicationContext.getStartupDate());
        return new ResponseEntity<String>(String.valueOf(appInfo), HttpStatus.ACCEPTED);
    }


    @GetMapping(value = "/envMode")
    @RequiresRoles(hasRoles = {"ROLE_MODERATOR", "ROLE_STUDENT"})
    public ResponseEntity<?> testDeneme() {
        List<String> list = System.getenv().values().stream()
                .map(itr -> itr)
                .collect(Collectors.toList());
        return new ResponseEntity<List>(list, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/env")
    @RequiresRoles(hasRoles = {"ROLE_ADMIN"})
    public ResponseEntity<?> testDeneme2() {
        List<String> list = System.getenv().values().stream()
                .map(itr -> itr)
                .collect(Collectors.toList());
        return new ResponseEntity<List>(list, HttpStatus.ACCEPTED);
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStudents() {
        List<StudentModel> studentModelList = studentRepository.findAll();
        return new ResponseEntity<List<StudentModel>>(studentModelList, HttpStatus.OK);
    }
}