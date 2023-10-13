package life.totl.totlback.users.repository;

import life.totl.totlback.logs.models.UserLogsBalesEntity;
import life.totl.totlback.users.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserName(String userName);
    UserEntity findByUserEmail(String userEmail);
    Boolean existsByUserEmail(String userEmail);
    Boolean existsByUserName(String userName);
    UserLogsBalesEntity findByUserLogsBalesEntityId(Long id);
}
