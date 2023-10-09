package life.totl.totlback.users.repository;

import life.totl.totlback.users.models.ProfilePictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfilePictureRepository extends JpaRepository<ProfilePictureEntity, Long> {

    Optional<ProfilePictureEntity> findByUserId(String name);
    @Override
    void deleteById(Long aLong);
}
