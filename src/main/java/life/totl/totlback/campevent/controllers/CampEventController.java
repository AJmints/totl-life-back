package life.totl.totlback.campevent.controllers;

import life.totl.totlback.backpack.models.BackPackConfigurationEntity;
import life.totl.totlback.backpack.models.dtos.BackPackConfigDTO;
import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.models.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping(value = "/campevent")
public class CampEventController {

    private final JWTGenerator jwtGenerator;

    @Autowired
    private CampEventController(JWTGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping(value = "/createEvent")
    public ResponseEntity<?> createUserEvent(@RequestHeader("auth-token") String token,@RequestBody Object packConfigDTO) {
//        try {
//            if (!jwtGenerator.validateToken(token.substring(7, token.length()))){
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
//        }



        return ResponseEntity.status(HttpStatus.OK).body(packConfigDTO);
    }

    @GetMapping(value = "/getUserEvent/{user}/{eventId}")
    public ResponseEntity<?> getUserEvent() {

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("getUserEvent"));
    }

    @GetMapping(value = "/getAllRelevantEvents/{user}")
    public ResponseEntity<?> getAllRelevantEvents() {

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("getAllRelevantEvents"));
    }

    @DeleteMapping(value = "/deleteUsersEvent/{user}/{eventId}")
    public ResponseEntity<?> deleteUsersEvent(@RequestHeader("auth-token") String token) {

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("deleteUsersEvent"));
    }

}
