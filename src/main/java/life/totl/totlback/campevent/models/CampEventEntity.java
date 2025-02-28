package life.totl.totlback.campevent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class CampEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(mappedBy = "relatedCampEvents")
    private List<CampEventsRelatedToUserEntity> eventConnections;

    @ManyToOne
    @JoinColumn(name = "user_made_campevents", referencedColumnName = "id")
    @JsonIgnore
    private CampEventsRelatedToUserEntity createBy;
    private Date createDate;

    private String eventName;
    private Boolean isPrivate;
    private String startDate;
    private String startTime;
    private Date eventStart;
    private String endDate;
    private String endTime;
    private Date eventEnd;
    private String eventDetails;

    private String state;
    private String parkName;
    private String parkAddress;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "park_campground_detail", referencedColumnName = "id")
    private ParkDetailEntity campGround;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventGearRec")
    private List<ItemRecEntity> gearRecItems;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventMealRec")
    private List<MealRecEntity> eventMeals;
    @ManyToMany
    @JoinTable(
            name = "invited_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "invited_event_id"))
    private List<CampEventsRelatedToUserEntity> inviteList;

    public CampEventEntity() {
    }

    public CampEventEntity(CampEventsRelatedToUserEntity createBy, Date createDate, String eventName, List<CampEventsRelatedToUserEntity> inviteList, List<MealRecEntity> eventMeals, List<ItemRecEntity> gearRecItems, ParkDetailEntity campGround, String parkAddress, String parkName, String state, String eventDetails, Date eventEnd, String endTime, String endDate, Date eventStart, String startTime, String startDate, Boolean isPrivate) {
        this.createBy = createBy;
        this.createDate = createDate;
        this.eventName = eventName;
        this.inviteList = inviteList;
        this.eventMeals = eventMeals;
        this.gearRecItems = gearRecItems;
        this.campGround = campGround;
        this.parkAddress = parkAddress;
        this.parkName = parkName;
        this.state = state;
        this.eventDetails = eventDetails;
        this.eventEnd = eventEnd;
        this.endTime = endTime;
        this.endDate = endDate;
        this.eventStart = eventStart;
        this.startTime = startTime;
        this.startDate = startDate;
        this.isPrivate = isPrivate;
    }
}
