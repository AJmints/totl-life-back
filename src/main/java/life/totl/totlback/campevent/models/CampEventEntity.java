package life.totl.totlback.campevent.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CampEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // private UserEventsEntity createdByUser;
    // Private Date createDate;

    // private String eventName;
    // private boolean isPrivate;
    // private String or Date startDate + startTime;
    // private String or Date endDate + endTime;
    // private String parkState;
    // private String addressString;
    // private String parkName;
    // private String parkLat;
    // private String parkLong;
    // private String eventDetails;

    // @OneToMany ( Create DataBase for Saving Park Details to eventually populate list on front end)
    // private CampParkDetailEntity campParkDetails;



}
