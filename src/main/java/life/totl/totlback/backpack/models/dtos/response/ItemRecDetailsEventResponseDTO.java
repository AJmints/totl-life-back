package life.totl.totlback.backpack.models.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemRecDetailsEventResponseDTO {

    private Long id;
    private String category;
    private String model;
    private String type;
    private String extraInfo;

}
