package life.totl.totlback.campevent.models.dtos.responses;

import life.totl.totlback.backpack.models.dtos.response.ItemRecDetailsEventResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventItemRecDetailResponsDTO {

    private long id;
    private int count;
    private String perGroupOrPerson;
    private ItemRecDetailsEventResponseDTO itemDetails;

}
