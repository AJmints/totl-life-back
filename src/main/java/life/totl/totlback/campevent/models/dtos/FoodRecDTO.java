package life.totl.totlback.campevent.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FoodRecDTO {

    public Integer id;
    public String bfast;
    public String lunch;
    public String dinner;
    public String snacks;
    public String notes;

}
