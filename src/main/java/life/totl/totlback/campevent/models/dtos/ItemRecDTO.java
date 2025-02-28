package life.totl.totlback.campevent.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ItemRecDTO {

    private String category;
    private String groupType;
    private Integer count;
    private String gearType;

    public String getGearType() {
        String[] tokens = gearType.split(" - ");
        return tokens[1];
    }
}
