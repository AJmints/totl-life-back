package life.totl.totlback.logs.repositories;

import life.totl.totlback.logs.models.LogsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogsEntityRepository extends JpaRepository<LogsEntity, Long> {

    Optional<LogsEntity> findByLogName(String logName);
    Boolean existsByLogName(String logName);
}
