package life.totl.totlback.services.email;

import life.totl.totlback.models.EmailConfirmTokenEntity;
import life.totl.totlback.models.UserEntity;
import life.totl.totlback.repository.EmailConfirmTokenRepository;
import life.totl.totlback.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service(value = "emailService")
public class EmailService {

    private JavaMailSender javaMailSender;
    private EmailConfirmTokenRepository emailConfirmTokenRepository;
    private UserEntityRepository userEntityRepository;

    @Autowired
    public EmailService(JavaMailSender javaMailSender,EmailConfirmTokenRepository emailConfirmTokenRepository, UserEntityRepository userEntityRepository) {
        this.javaMailSender = javaMailSender;
        this.emailConfirmTokenRepository = emailConfirmTokenRepository;
        this.userEntityRepository = userEntityRepository;
    }
    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public String confirmEmail(String confirmationToken) {

        if (emailConfirmTokenRepository.existsByConfirmationToken(confirmationToken)) {
            return "Error: Token does not exist";
        }

        EmailConfirmTokenEntity token = emailConfirmTokenRepository.findByConfirmationToken(confirmationToken);
        Date currentDate = new Date();

        if (token.getExpiryDate().before(currentDate)) {
            return "Email link has expired, please request a new link and try again.";
        } else {
            Optional<UserEntity> user = userEntityRepository.findById(token.getUserId());
            if (user.isPresent()) {
                user.get().setAccountVerified(true);
                userEntityRepository.save(user.get());
                return "Email verified successfully!";
            } else {
                return "Error: Something went wrong, couldn't verify email.";
            }

        }



    }


}
