package life.totl.totlback.logs.repositories;

import life.totl.totlback.logs.models.LogsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsEntityRepository extends JpaRepository<LogsEntity, Long> {
}
