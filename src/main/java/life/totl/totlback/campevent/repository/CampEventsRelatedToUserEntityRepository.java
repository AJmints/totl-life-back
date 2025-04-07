package life.totl.totlback.campevent.repository;

import life.totl.totlback.campevent.models.CampEventsRelatedToUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampEventsRelatedToUserEntityRepository extends JpaRepository<CampEventsRelatedToUserEntity, Long> {
//    CampEventsRelatedToUserEntity findByCampEventsRelatedToUserEntity(Long id);
}
