package life.totl.totlback.repository;

import life.totl.totlback.models.EmailConfirmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailConfirmTokenRepository extends JpaRepository<EmailConfirmTokenEntity, Long> {
    EmailConfirmTokenEntity findByConfirmationToken(String confirmationToken);
    Boolean existsByConfirmationToken(String confirmationToken);
}
