package life.totl.totlback.campevent.repository;

import life.totl.totlback.campevent.models.CampEventEntity;
import life.totl.totlback.campevent.models.CampEventsRelatedToUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampEventEntityRepository extends JpaRepository<CampEventEntity, Long> {

}
