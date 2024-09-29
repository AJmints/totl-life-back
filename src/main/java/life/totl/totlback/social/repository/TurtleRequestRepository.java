package life.totl.totlback.social.repository;

import life.totl.totlback.social.models.SocialUserHubEntity;
import life.totl.totlback.social.models.TurtleRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurtleRequestRepository extends JpaRepository<TurtleRequestEntity, Long> {
    Boolean existsByRequesterAndRequested(SocialUserHubEntity requester, SocialUserHubEntity requested);
    TurtleRequestEntity findByRequesterAndRequested(SocialUserHubEntity requester, SocialUserHubEntity requested);
    List<TurtleRequestEntity> findAllByRequested(SocialUserHubEntity requested);

}
