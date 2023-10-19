package life.totl.totlback.logs.repositories;

import life.totl.totlback.logs.models.BalesEntity;
import life.totl.totlback.logs.models.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByParentBale(BalesEntity id);
}
