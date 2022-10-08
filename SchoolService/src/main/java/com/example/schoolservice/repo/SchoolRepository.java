package com.example.schoolservice.repo;


import com.example.schoolservice.model.SchoolModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SchoolRepository  extends JpaRepository<SchoolModel, Long> {
}