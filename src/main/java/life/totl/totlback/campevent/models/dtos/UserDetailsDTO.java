package life.totl.totlback.campevent.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserDetailsDTO {

    public String eventName;
    public String host;
    public String dateRange;
    public int goingNum;
    public String typeEvent;
    public Long eventID;

}
