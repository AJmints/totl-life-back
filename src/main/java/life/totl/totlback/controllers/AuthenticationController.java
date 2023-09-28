package life.totl.totlback.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerNewUser() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
