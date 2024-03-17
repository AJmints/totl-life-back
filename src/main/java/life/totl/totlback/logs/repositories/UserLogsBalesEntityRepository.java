package life.totl.totlback.logs.repositories;

import life.totl.totlback.logs.models.BalesEntity;
import life.totl.totlback.logs.models.UserLogsBalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLogsBalesEntityRepository extends JpaRepository<UserLogsBalesEntity, Long> {

}
