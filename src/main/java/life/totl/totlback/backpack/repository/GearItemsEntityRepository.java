package life.totl.totlback.backpack.repository;

import life.totl.totlback.backpack.models.GearItemsEntity;
import life.totl.totlback.backpack.models.dtos.GearItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GearItemsEntityRepository extends JpaRepository<GearItemsEntity, Long> {
        List<GearItemsEntity> findAllByCategory(String category);
        List<GearItemsEntity> findAllByType(String type);
        boolean existsByCategoryAndTypeAndBrand(String category, String type, String brand);
        GearItemsEntity findByCategoryAndTypeAndBrand(String category, String type, String brand);
}
