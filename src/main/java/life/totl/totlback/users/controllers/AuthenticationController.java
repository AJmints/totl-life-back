package life.totl.totlback.users.controllers;

import life.totl.totlback.logs.repositories.UserLogsBalesEntityRepository;
import life.totl.totlback.security.payload.response.JWTResponse;
import life.totl.totlback.security.utils.jwt.JWTGenerator;
import life.totl.totlback.users.models.EmailConfirmTokenEntity;
import life.totl.totlback.users.models.ProfilePictureEntity;
import life.totl.totlback.users.models.dtos.EmailConfirmationTokenDTO;
import life.totl.totlback.users.models.dtos.LoginDTO;
import life.totl.totlback.users.models.response.ResponseMessage;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.models.dtos.UserEntityDTO;
import life.totl.totlback.users.repository.EmailConfirmTokenRepository;
import life.totl.totlback.users.repository.ProfilePictureRepository;
import life.totl.totlback.users.repository.UserEntityRepository;
import life.totl.totlback.users.services.email.EmailService;
import life.totl.totlback.users.utils.ImageUtility;
import life.totl.totlback.users.utils.TotlUserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping(value = "/auth")
public class AuthenticationController {

    private final TotlUserProperties totlUserProperties;
    private final UserEntityRepository userEntityRepository;
    private final EmailConfirmTokenRepository emailConfirmTokenRepository;
    private final UserLogsBalesEntityRepository userLogsBalesEntityRepository;
    private final ProfilePictureRepository profilePictureRepository;
    private final EmailService emailService;
    private final JWTGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    private AuthenticationController(TotlUserProperties totlUserProperties, UserEntityRepository userEntityRepository, EmailConfirmTokenRepository emailConfirmTokenRepository, EmailService emailService, JWTGenerator jwtGenerator, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserLogsBalesEntityRepository userLogsBalesEntityRepository, ProfilePictureRepository profilePictureRepository) {
        this.totlUserProperties = totlUserProperties;
        this.userEntityRepository = userEntityRepository;
        this.emailConfirmTokenRepository = emailConfirmTokenRepository;
        this.userLogsBalesEntityRepository = userLogsBalesEntityRepository;
        this.profilePictureRepository = profilePictureRepository;
        this.emailService = emailService;
        this.jwtGenerator = jwtGenerator;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping(value = "/register")
    public ResponseEntity<?> registerNewUser(@RequestBody UserEntityDTO userEntityDTO) {
        /* Does this user already exist? */
        if (userEntityRepository.existsByUserEmail(userEntityDTO.getUserEmail())) {
            ResponseMessage message = new ResponseMessage("That email is already in use, did you forget your password?");
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (userEntityRepository.existsByUserName(userEntityDTO.getUserName())) {
            ResponseMessage message = new ResponseMessage("That name is taken, please select a different name.");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        List<String> role = new ArrayList<>();
        role.add("USER");

        /* Create and save new user */
        UserEntity user = new UserEntity(userEntityDTO.getUserName().toLowerCase(), userEntityDTO.getUserEmail().toLowerCase(), userEntityDTO.getPassword(), false);
        user.setRoles(role);
        userEntityRepository.save(user);
        user.getUserPFP().setUserId(String.valueOf(user.getId()));

        /* Generate confirmation token details */
        EmailConfirmTokenEntity confirmationToken = new EmailConfirmTokenEntity();
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setUserId(user.getId());
        confirmationToken.setExpiryDate();

        emailConfirmTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUserEmail());
        mailMessage.setSubject("Verify your TOTL.life account");
        mailMessage.setText("Hello, " + user.getUserName() + "\n\nThank you for joining TOTL.life! Please follow the link to verify your account so you can enjoy everything TOTL.life has to offer! Link is valid for 2 hours.\n\n" + totlUserProperties.getHostUrl() + "/register/confirm/" + confirmationToken.getConfirmationToken());

        emailService.sendEmail(mailMessage);
        ResponseMessage response = new ResponseMessage("You have successfully registered! Check your email to complete your verification. If you don't see the email in your primary, check your spam.");

        return new ResponseEntity<>(response,HttpStatus.OK);
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
            if (user.isPresent()) {
                /* Delete extra tokens associated with the user account that is creating a new token */
                for (EmailConfirmTokenEntity tokens : emailConfirmTokenRepository.findAllByUserId(user.get().getId())) {
                    /** TODO: Set up Hash comparison method on EmailConfirmTokenEntity */
                    if (!Objects.equals(tokenCheck, tokens)) {
                        emailConfirmTokenRepository.delete(tokens);
                    }
                }
            }
        } else {
            ResponseMessage response = new ResponseMessage("This link is no longer valid, please go to your profile page and try again through verify settings.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        /* Create new token */
        EmailConfirmTokenEntity confirmationToken = new EmailConfirmTokenEntity();
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setUserId(user.get().getId());
        confirmationToken.setExpiryDate();
        emailConfirmTokenRepository.save(confirmationToken);

        /* Send new Email */
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.get().getUserEmail());
        mailMessage.setSubject("Verify your TOTL.life account - resend");
        mailMessage.setText("Hello, " + user.get().getUserName() + "\n\nThank you for joining TOTL.life! Please follow the link to verify your account so you can enjoy everything TOTL.life has to offer! Link is valid for 2 hours.\n\n" + totlUserProperties.getHostUrl() + "/register/confirm/" + confirmationToken.getConfirmationToken());

        emailService.sendEmail(mailMessage);
        ResponseMessage response = new ResponseMessage("Email sent successfully! Please check your email and follow the link to verify your account.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        Optional<UserEntity> userEntity = Optional.ofNullable(userEntityRepository.findByUserEmail(loginDTO.getUserEmail()));

        if (userEntity.isPresent()) {
            if (!userEntity.get().isMatchingPassword(loginDTO.getUserPassword())) {
                ResponseMessage response = new ResponseMessage("failed", "Passwords don't match");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        if (userEntity.isPresent()) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userEntity.get().getUserName(),
                            loginDTO.getUserPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            JWTResponse response;
            if (Arrays.equals(userEntity.get().getUserPFP().getImage(), new byte[256])) {
                response = new JWTResponse(token,
                        userEntity.get().getId(),
                        userEntity.get().getUserName(),
                        userEntity.get().getUserEmail(),
                        userEntity.get().getRoles(),
                        userEntity.get().isAccountVerified());
            } else {
                response = new JWTResponse(token,
                        userEntity.get().getId(),
                        userEntity.get().getUserName(),
                        userEntity.get().getUserEmail(),
                        userEntity.get().getRoles(),
                        ProfilePictureEntity.builder().image(ImageUtility.decompressImage(userEntity.get().getUserPFP().getImage())).build().getImage(),
                        userEntity.get().isAccountVerified());
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
