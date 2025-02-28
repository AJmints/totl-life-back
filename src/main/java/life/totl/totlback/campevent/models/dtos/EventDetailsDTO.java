package life.totl.totlback.campevent.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class EventDetailsDTO {

    private String eventName;
    private Boolean isPrivate;
    private String startDate;
    private String startTime;
    private String eventStart;
    private String endDate;
    private String endTime;
    private String eventEnd;
    private String userDescription;
    private String parkState;

    private ParkDetailDTO campGround;
}
