package com.example.authservice.commonhandler;

import com.example.authservice.domain.Erole;
import com.example.authservice.domain.RoleModel;
import com.example.authservice.domain.UserModel;
import com.example.authservice.domain.payload.SignupRequest;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AuthenticationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @tnhncnn
 * @see
 */

@Configuration
public class RoleLoader {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;



    @Value("${spring.profiles.active}")
    private String activeProfile;

    private List<String> mergeList(List<String> listOne, List<String> listTwo) {
        List<String> combinedList = Stream.of(listOne, listTwo)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
        return combinedList;
    }

    /**
     * @param master
     * @param slave
     * @return  compareAndDifferentObject
     */
    private List<String> compareAndDifferentObject(List<String> master, List<String> slave) {

        List<String> differences = slave.stream()
                .filter(element -> !master.contains(element))
                .collect(Collectors.toList());
        return differences;
    }

    @PostConstruct
    @Order(1)
    public void roleLoader() {
        System.out.println("active profile is -> " + activeProfile);

        List<RoleModel> roleList = new ArrayList<>();
        roleList = roleRepository.findAll();
        List<String> enumNames = Stream.of(Erole.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        if (roleList.isEmpty()) {
            for (String iterator : enumNames) {
                System.out.print("add to ->" + iterator + " - ");
                RoleModel temp = new RoleModel();
                temp.setRoleName(String.valueOf(Erole.valueOf(iterator)));
                roleRepository.save(temp);
            }
        } else {
            for (String iterator : compareAndDifferentObject(roleList.stream().map(itr -> itr.getRoleName()).collect(Collectors.toList()), enumNames)) {
                System.out.print("add to ->" + iterator + " - ");
                RoleModel temp = new RoleModel();
                temp.setRoleName(String.valueOf(Erole.valueOf(iterator)));
                roleRepository.save(temp);
            }
        }

    }

    @PostConstruct
    @Order(2)
    private void adminLoader(){
        Optional<UserModel> user = userRepository.findByUsername("ADMIN") ;
        if(!user.isPresent()){
            SignupRequest adminReq = new SignupRequest();
            adminReq.setEmail("admin@huawei.com");
            Set<String> role = new HashSet<>();
            role.add("ADMIN");
            adminReq.setRole( role );
            adminReq.setUsername("ADMIN");
            adminReq.setPassword("12345678");
            authenticationService.register(adminReq);
        }
    }


}