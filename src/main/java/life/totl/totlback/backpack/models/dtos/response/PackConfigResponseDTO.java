package life.totl.totlback.backpack.models.dtos.response;

import life.totl.totlback.backpack.models.BackPackConfigurationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PackConfigResponseDTO {

    public String status;
    public BackPackConfigurationEntity newPack;
}
