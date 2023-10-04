package life.totl.totlback.controllers;

import jakarta.transaction.Transactional;
import life.totl.totlback.models.EmailConfirmTokenEntity;
import life.totl.totlback.models.dtos.EmailConfirmationTokenDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        mailMessage.setText("Hello, " + user.getUserName() + "\n\nThank you for joining TOTL.life! Please follow the link to verify your account so you can enjoy everything TOTL.life has to offer! Link is valid for 2 hours from now.\n\n" + totlProperties.getHostUrl() + "/register/confirm/" + confirmationToken.getConfirmationToken());

        emailService.sendEmail(mailMessage);


        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<?> confirmUserAccount(@RequestBody EmailConfirmationTokenDTO token) {

        if (!emailConfirmTokenRepository.existsByConfirmationToken(token.getTokenId())) {
            ResponseMessage message = new ResponseMessage("The confirmation link is invalid, you can create a new link and try again.");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

        ResponseMessage message = new ResponseMessage(emailService.confirmEmail(token.getTokenId()));

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping(value = "/new-link")
    public ResponseEntity<?> sendNewConfirmEmail(@RequestBody EmailConfirmationTokenDTO token) {
        Optional<EmailConfirmTokenEntity> tokenCheck = Optional.ofNullable(emailConfirmTokenRepository.findByConfirmationToken(token.getTokenId()));
        Optional<UserEntity> user = Optional.empty();

        if (tokenCheck.isPresent()) {
            user = userEntityRepository.findById(tokenCheck.get().getUserId());

            /** Delete extra tokens associated with the user account that is getting a new token **/
            for (EmailConfirmTokenEntity tokens : emailConfirmTokenRepository.findAllByUserId(user.get().getId())) {
                if (!Objects.equals(tokenCheck, tokens)) {
                    emailConfirmTokenRepository.delete(tokens);
                }
            }

        } else {
            ResponseMessage response = new ResponseMessage("This link is no longer valid, please go to your profile page and try again through verified settings.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        /* Create new token */
        EmailConfirmTokenEntity confirmationToken = new EmailConfirmTokenEntity();
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setUserId(user.get().getId());
        confirmationToken.setExpiryDate();
        emailConfirmTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.get().getUserEmail());
        mailMessage.setSubject("Verify your TOTL.life account - resend");
        mailMessage.setText("Hello, " + user.get().getUserName() + "\n\nThank you for joining TOTL.life! Please follow the link to verify your account so you can enjoy everything TOTL.life has to offer! Link is valid for 2 hours from now.\n\n" + totlProperties.getHostUrl() + "/register/confirm/" + confirmationToken.getConfirmationToken());

        emailService.sendEmail(mailMessage);

        ResponseMessage response = new ResponseMessage("Email sent successfully! Please check your email and follow the link to verify your account.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
