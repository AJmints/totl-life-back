package life.totl.totlback.controllers;

import life.totl.totlback.models.EmailConfirmTokenEntity;
import life.totl.totlback.models.response.ResponseMessage;
import life.totl.totlback.models.UserEntity;
import life.totl.totlback.models.dtos.UserEntityDTO;
import life.totl.totlback.repository.EmailConfirmTokenRepository;
import life.totl.totlback.repository.UserEntityRepository;
import life.totl.totlback.services.email.EmailService;
import life.totl.totlback.utils.TotlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/auth")
public class AuthenticationController {

    private final TotlProperties totlProperties;
    private final UserEntityRepository userEntityRepository;
    private final EmailConfirmTokenRepository emailConfirmTokenRepository;
    private final EmailService emailService;


    @Autowired
    private AuthenticationController(TotlProperties totlProperties,  UserEntityRepository userEntityRepository, EmailConfirmTokenRepository emailConfirmTokenRepository,EmailService emailService) {
        this.totlProperties = totlProperties;
        this.userEntityRepository = userEntityRepository;
        this.emailConfirmTokenRepository = emailConfirmTokenRepository;
        this.emailService = emailService;
    }


    @PostMapping(value = "/register")
    public ResponseEntity<?> registerNewUser(@RequestBody UserEntityDTO userEntityDTO) {
        /* Does this user already exist? */
        if (userEntityRepository.existsByUserEmail(userEntityDTO.getUserEmail())) {
            System.out.println(userEntityDTO.getUserEmail());
            ResponseMessage message = new ResponseMessage("That email is already in use, did you forget your password?");
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (userEntityRepository.existsByUserName(userEntityDTO.getUserName())) {
            ResponseMessage message = new ResponseMessage("That name is taken, please select a different name.");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        List<String> role = new ArrayList<>();
        role.add("USER");

        /* Create and save new user */
        UserEntity user = new UserEntity(userEntityDTO.getUserName(), userEntityDTO.getUserEmail(), userEntityDTO.getPassword(), false, role);
        userEntityRepository.save(user);

        /* Generate confirmation token details */
        EmailConfirmTokenEntity confirmationToken = new EmailConfirmTokenEntity();
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setUserId(user.getId());
        confirmationToken.setExpiryDate();

        emailConfirmTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUserEmail());
        mailMessage.setSubject("Verify your TOTL.life account");
        mailMessage.setText("Hello, " + user.getUserName() + "\n\nThank you for joining TOTL.life! Please follow the link to verify your account so you can enjoy everything TOTL.life has to offer!\n\n" + totlProperties.getHostUrl() + "/auth/confirm/" + confirmationToken.getConfirmationToken());

        emailService.sendEmail(mailMessage);


        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
