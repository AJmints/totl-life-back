package life.totl.totlback.users.services.email;

import life.totl.totlback.users.models.EmailConfirmTokenEntity;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.repository.EmailConfirmTokenRepository;
import life.totl.totlback.users.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service(value = "emailHelper")
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailConfirmTokenRepository emailConfirmTokenRepository;
    private final UserEntityRepository userEntityRepository;

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

        Optional<EmailConfirmTokenEntity> token = Optional.ofNullable(emailConfirmTokenRepository.findByConfirmationToken(confirmationToken));

        if (token.isEmpty()) {
            return "Error: Token does not exist";
        }

        if (token.get().getExpiryDate().before(new Date())) {
            return "Email link has expired, please request a new link and try again.";
        } else {
            Optional<UserEntity> user = userEntityRepository.findById(token.get().getUserId());
            if (user.isPresent()) {
                if (user.get().isAccountVerified()) {
                    return "You're account is already verified";
                }

                user.get().setAccountVerified(true);
                userEntityRepository.save(user.get());
                /** TODO: Set up proper way to delete old confirmation tokens from repository
                 * this approach causes and error **/
//                emailConfirmTokenRepository.delete(token.get());
                return "Email verified successfully!";

            } else {
                return "Error: Something went wrong, couldn't verify email.";
            }

        }



    }


}
