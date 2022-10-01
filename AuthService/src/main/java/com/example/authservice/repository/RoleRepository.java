package com.example.authservice.repository;


import com.example.authservice.domain.Erole;
import com.example.authservice.domain.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel,String> {
    Optional<RoleModel> findByRoleName(Erole roleName);
}
