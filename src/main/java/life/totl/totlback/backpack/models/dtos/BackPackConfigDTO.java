package life.totl.totlback.backpack.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BackPackConfigDTO {

    private Long userID;
    private List<Long> specificGearItems; //Item IDs , Item Name or other identifier
    private String configType;
    private String packName;
    private String packNotes;
    private Boolean hiddenPack;


}
