package life.totl.totlback.campevent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime eventStart;
    private String endDate;
    private String endTime;
    private LocalDateTime eventEnd;
    @Column(columnDefinition = "VARCHAR(6000)")
    private String eventDetails;

    private String state;
    private String parkName;
    private String parkAddress;

    @ManyToOne
    @JoinColumn(name = "park_campground_detail", referencedColumnName = "id")
    @JsonIgnore
    private ParkDetailEntity campGround;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventGearRec")
    private List<ItemRecEntity> gearRecItems;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventMealRec")
    private List<MealRecEntity> eventMeals;
    @ManyToMany
    @JoinTable(
            name = "events_guest_list",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<CampEventsRelatedToUserEntity> inviteList;

    public CampEventEntity() {
    }

    public CampEventEntity(CampEventsRelatedToUserEntity createBy, Date createDate, String eventName, List<CampEventsRelatedToUserEntity> inviteList, List<MealRecEntity> eventMeals, List<ItemRecEntity> gearRecItems, String parkAddress, String parkName, String state, String eventDetails, LocalDateTime eventEnd, String endTime, String endDate, LocalDateTime eventStart, String startTime, String startDate, Boolean isPrivate, ParkDetailEntity parkDetail) {
        this.createBy = createBy;
        this.createDate = createDate;
        this.eventName = eventName;
        this.inviteList = inviteList;
        this.eventMeals = eventMeals;
        this.gearRecItems = gearRecItems;
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
        this.campGround = parkDetail;
    }

    @PreRemove
    private void removeParkDetailEntity() {
        this.campGround.removeParkDetailEntity(this);
    }
}
