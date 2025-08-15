package life.totl.totlback.campevent.models.dtos.responses;

import life.totl.totlback.campevent.models.ItemRecEntity;
import life.totl.totlback.campevent.models.MealRecEntity;
import life.totl.totlback.campevent.models.ParkDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpecificEventResponseDTO {

    private Long id;
    private String createdBy;  // Additional
    private Date createDate;
    private String eventName;
    private Boolean isPrivate;
    private String eventType;
    private String startDate;
    private String startTime;
    private LocalDateTime eventStart;
    private String endDate;
    private String endTime;
    private LocalDateTime eventEnd;
    private String eventDetails;
    private String state;
    private String parkName;
    private String parkAddress;

    private ParkDetailEntity ParkDetail; // placeholder, make new dto for ParkDetailEntity

    private List<InviteListEventResponseDTO> inviteList = new ArrayList<>(); // done

    private List<EventItemRecDetailResponsDTO> gearRecItems = new ArrayList<>();

    private List<MealRecEntity> eventMeals = new ArrayList<>();

    private List<String> usersGearList;

    public SpecificEventResponseDTO(Long id, String createdBy, Date createDate, String eventName, Boolean isPrivate, String eventType, String startDate, String parkAddress, String parkName, String state, String eventDetails, LocalDateTime eventEnd, String endTime, String endDate, LocalDateTime eventStart, String startTime) {
        this.id = id;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.eventName = eventName;
        this.isPrivate = isPrivate;
        this.eventType = eventType;
        this.startDate = startDate;
        this.parkAddress = parkAddress;
        this.parkName = parkName;
        this.state = state;
        this.eventDetails = eventDetails;
        this.eventEnd = eventEnd;
        this.endTime = endTime;
        this.endDate = endDate;
        this.eventStart = eventStart;
        this.startTime = startTime;
    }
}
