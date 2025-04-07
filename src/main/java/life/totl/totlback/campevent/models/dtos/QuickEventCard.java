package life.totl.totlback.campevent.models.dtos;

import life.totl.totlback.campevent.models.CampEventsRelatedToUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class QuickEventCard {

    public String eventName;
    public String host;
    public String dateRange;
    public int goingNum;
    public String typeEvent;
    public Long eventID;

    public QuickEventCard(String eventName, String dateRange, int goingNum, String typeEvent, Long eventID) {
        this.eventName = eventName;
        this.dateRange = dateRange;
        this.goingNum = goingNum;
        this.typeEvent = typeEvent;
        this.eventID = eventID;
    }
}
