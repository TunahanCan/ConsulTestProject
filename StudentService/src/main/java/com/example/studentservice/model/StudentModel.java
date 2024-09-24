package com.example.studentservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity(name = "student_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long student_id;


    @Column(name = "student_name")
    @Size(max = 30)
    private String studentName;

    @Column(name = "age")
    private int age;

    @Column(name = "school_name")
    private String schoolName;


    public StudentModel(String studentName, int age, String schoolName) {
        this.studentName = studentName;
        this.age = age;
        this.schoolName = schoolName;
    }


}