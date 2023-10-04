package life.totl.totlback.repository;

import life.totl.totlback.models.EmailConfirmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailConfirmTokenRepository extends JpaRepository<EmailConfirmTokenEntity, Long> {
    EmailConfirmTokenEntity findByConfirmationToken(String confirmationToken);
    Boolean existsByConfirmationToken(String confirmationToken);

    List<EmailConfirmTokenEntity> findAllByUserId(Long id);
}
