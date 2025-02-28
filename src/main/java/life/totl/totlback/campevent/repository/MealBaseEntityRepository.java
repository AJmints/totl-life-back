package life.totl.totlback.campevent.repository;

import life.totl.totlback.campevent.models.MealBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealBaseEntityRepository extends JpaRepository<MealBaseEntity, Long> {
    MealBaseEntity findByBfastAndLunchAndDinnerAndSnacks(String bfast, String lunch, String dinner, String snacks);
    Boolean existsByBfastAndLunchAndDinnerAndSnacks(String bfast, String lunch, String dinner, String snacks);
}
