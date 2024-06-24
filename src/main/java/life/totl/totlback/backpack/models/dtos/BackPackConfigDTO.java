package life.totl.totlback.backpack.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class BackPackConfigDTO {

    private Long userID;
    private List<Map<Long, String>> SpecificGearItems; //Item IDs , Item Name or other identifier
    private String configType;
    private String packName;
    private boolean hidden;


}
