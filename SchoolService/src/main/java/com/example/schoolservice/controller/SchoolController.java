package com.example.schoolservice.controller;



import com.example.schoolservice.exception.ApiException;
import com.example.schoolservice.model.SchoolModel;
import com.example.schoolservice.repo.SchoolRepository;
import com.example.schoolservice.security.RequiresRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping(value = "/school")
@Slf4j
public class SchoolController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SchoolRepository schoolRepository;

    @GetMapping(value = "/getSchoolDetails")
    public String getStudents(@NotNull @RequestParam("schoolName") String schoolName)
    {
        System.out.println("Getting School details for " + schoolName);
        String response = restTemplate.exchange("http://student-service/student/getStudentListForSchool/{schoolname}",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, schoolName).getBody();
        System.out.println("Response Received as " + response);
        return "School Name -  " + schoolName + " \n Student Details " + response;
    }

    @GetMapping(value = "/getAllSchool")
    @RequiresRoles(hasRoles = {"ROLE_ADMIN"})
    public ResponseEntity<?> getAllSchool(){
        log.error("get getAllSchool service ");
        List<SchoolModel> list = schoolRepository.findAll();
        if (list.isEmpty())
            throw new ApiException("Student List is empty" , HttpStatus.NO_CONTENT);

        return new ResponseEntity<List>(list,HttpStatus.OK) ;
    }


}