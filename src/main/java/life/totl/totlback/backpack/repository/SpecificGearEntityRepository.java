package life.totl.totlback.backpack.repository;

import life.totl.totlback.backpack.models.UserSpecificGearEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecificGearEntityRepository extends JpaRepository<UserSpecificGearEntity, Long> {
}
