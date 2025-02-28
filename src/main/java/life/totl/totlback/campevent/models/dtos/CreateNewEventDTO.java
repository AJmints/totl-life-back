package life.totl.totlback.campevent.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CreateNewEventDTO {

    private EventDetailsDTO eventDetails;
    private List<ItemRecDTO> gearRecDTO;
    private List<FoodRecDTO> mealPlan;
    private List<String> friendListString;
    private String userName;

}
