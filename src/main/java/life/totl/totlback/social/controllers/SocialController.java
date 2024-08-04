package life.totl.totlback.social.controllers;

import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.social.models.SocialUserHubEntity;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping(value = "/social")
public class SocialController {

    private final JWTGenerator jwtGenerator;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    private SocialController(JWTGenerator jwtGenerator, UserEntityRepository userEntityRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userEntityRepository = userEntityRepository;
    }

    @GetMapping(value = "/addSocial")
    public ResponseEntity<?> addSocial() {

        List<UserEntity> users = userEntityRepository.findAll();

//        for (UserEntity user : users) {
//                user.setSocialHub(new SocialUserHubEntity(user));
//                userEntityRepository.save(user);
//            }



        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
