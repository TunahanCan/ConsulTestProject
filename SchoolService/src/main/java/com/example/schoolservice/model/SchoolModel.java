package com.example.schoolservice.model;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "school_db")
public class SchoolModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private Long school_id;

    @Column(name = "school_name")
    @Size(max = 30)
    private String schoolName;


    @Column(name = "school_address")
    private String schoolAddress;


    public SchoolModel( String schoolName , String schoolAddress)
    {
        this.schoolAddress=schoolAddress;
        this.schoolName= schoolName;
    }

}