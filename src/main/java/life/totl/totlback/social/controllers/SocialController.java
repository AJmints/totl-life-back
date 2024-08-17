package life.totl.totlback.social.controllers;

import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.social.models.SocialUserHubEntity;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    @GetMapping(value = "/addSocial") // make callable with special button
    public ResponseEntity<?> addSocial() {

        List<UserEntity> users = userEntityRepository.findAll();
        HashMap<String, String> status = new HashMap<>();

        for (UserEntity user : users) {
            if (Objects.equals(user.getSocialHub(), null)) {
                user.setSocialHub(new SocialUserHubEntity(user));
                userEntityRepository.save(user);
                status.put(user.getUserName(), "Added");
            } else {
                status.put(user.getUserName(), "Already has");
            }

        }

        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @PostMapping(value = "/requestFriend")
    public ResponseEntity<?> requestFriend(@RequestHeader("auth-token") String token) {
        try {
            if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }

        /** Work on friend request **/
        return ResponseEntity.status(HttpStatus.OK).body("added");
    }
}
