package life.totl.totlback.logs.repositories;

import life.totl.totlback.logs.models.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {
}
