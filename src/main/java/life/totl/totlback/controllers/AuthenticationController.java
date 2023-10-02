package life.totl.totlback.controllers;

import life.totl.totlback.models.ResponseMessage;
import life.totl.totlback.models.UserEntity;
import life.totl.totlback.models.dtos.UserEntityDTO;
import life.totl.totlback.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(value = "/api")
public class AuthenticationController {

    private final UserEntityRepository userEntityRepository;

    @Autowired
    private AuthenticationController(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }


    @PostMapping(value = "/register")
    public ResponseEntity<?> registerNewUser(@RequestBody UserEntityDTO userEntityDTO) {
        List<String> role = new ArrayList<>();
        role.add("USER");

        UserEntity user = new UserEntity(userEntityDTO.getUserName(), userEntityDTO.getUserEmail(), userEntityDTO.getPassword(), true, role);

//        Optional<UserEntity> userTest = Optional.of(userEntityRepository.findByUserName(userEntityDTO.getUserName()));
//
//        if (userTest.get() != null) {
//            ResponseMessage response  = new ResponseMessage("That name is in use, please select a different name");
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
