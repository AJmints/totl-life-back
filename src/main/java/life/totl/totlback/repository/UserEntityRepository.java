package life.totl.totlback.repository;

import life.totl.totlback.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserName(String userName);
}
