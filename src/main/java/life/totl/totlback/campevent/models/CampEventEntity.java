package life.totl.totlback.campevent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import life.totl.totlback.campevent.models.dtos.modelhelpers.QuickEventCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    private List<CampEventsRelatedToUserEntity> eventConnections;

    @ManyToOne
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "user_made_campevents", referencedColumnName = "id")
    @JsonIgnore
    private CampEventsRelatedToUserEntity createBy;
    private Date createDate;

    private String eventName;
    private Boolean isPrivate;
    private String eventType; // Car Camping, Floating, Biking, etc
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
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "park_campground_detail", referencedColumnName = "id")
    @JsonIgnore
    private ParkDetailEntity campGround;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventGearRec")
    @Fetch(FetchMode.SELECT)
    private List<ItemRecEntity> gearRecItems;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventMealRec")
    @Fetch(FetchMode.SELECT)
    private List<MealRecEntity> eventMeals;
    @ManyToMany
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    @JoinTable(
            name = "events_guest_list",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<CampEventsRelatedToUserEntity> inviteList;

    public CampEventEntity() {
    }

    public CampEventEntity(CampEventsRelatedToUserEntity createBy, String eventType, Date createDate, String eventName, List<CampEventsRelatedToUserEntity> inviteList, List<MealRecEntity> eventMeals, List<ItemRecEntity> gearRecItems, String parkAddress, String parkName, String state, String eventDetails, LocalDateTime eventEnd, String endTime, String endDate, LocalDateTime eventStart, String startTime, String startDate, Boolean isPrivate, ParkDetailEntity parkDetail) {
        this.createBy = createBy;
        this.eventType = eventType;
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

    public QuickEventCard quickList() {
        return new QuickEventCard(this.eventName, this.startDate + "/" +this.endDate, this.getInviteList().size(), this.eventType, this.id);
    }

    public String viewCreatorOfEvent() {
        return this.createBy.getUser().getUserName();
    }
}
