package com.example.studentservice.controller;

import com.example.studentservice.exception.ApiException;
import com.example.studentservice.model.StudentModel;
import com.example.studentservice.repo.StudentRepository;
import com.example.studentservice.security.RequiresRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(value = "/student")
@Slf4j
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @GetMapping(value = "/getAllStudent")
    @RequiresRoles(hasRoles = {"ROLE_ADMIN"})
    public ResponseEntity<?> getStudents() {
        log.warn("Getting student");
        List<StudentModel> studentList = studentRepository.findAll();
        if (studentList.isEmpty()) {
            throw new ApiException("Student List is empty" , HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List>(studentList,HttpStatus.OK) ;
    }

    @RequestMapping(value = "/getStudentListForSchool/{schoolname}" , method = RequestMethod.GET)
    public ResponseEntity<List> getStudents(@PathVariable String schoolname) {
        log.error("Getting Student details for " + schoolname);
        List<StudentModel> studentList = studentRepository.findBySchoolName(schoolname) ;
        return new ResponseEntity<List>(studentList,HttpStatus.OK) ;
    }

    @GetMapping(value = "/getStudent/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable(value="studentId") Long studentId) {
        StudentModel studentModel = studentRepository.getById(studentId);
        if(studentModel != null)
            return new ResponseEntity<StudentModel>(studentModel,HttpStatus.OK) ;
        else  throw new ApiException("Student not find" , HttpStatus.NO_CONTENT);
    }

}