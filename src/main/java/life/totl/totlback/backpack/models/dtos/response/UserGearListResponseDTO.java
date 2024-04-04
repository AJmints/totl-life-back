package life.totl.totlback.backpack.models.dtos.response;

import life.totl.totlback.backpack.models.UserSpecificGearEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserGearListResponseDTO {

    public String status;
    public List<UserSpecificGearEntity> allUserGear;
    public UserSpecificGearEntity newGear;

    public UserGearListResponseDTO(String status, UserSpecificGearEntity newGear) {
        this.status = status;
        this.newGear = newGear;
    }

    public UserGearListResponseDTO(String status, List<UserSpecificGearEntity> allUserGear) {
        this.status = status;
        this.allUserGear = allUserGear;
    }
}
