package life.totl.totlback.campevent.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "park_detail")
@AllArgsConstructor
@Getter
@Setter
public class ParkDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "campGround")
    private CampEventEntity relatedCampEvent;

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    private String parkName;
    private String addressString;
    private String parkGovID;
    private String parkURL;
    private String latitude;
    private String longitude;
    private String addrLine1;
    private String addrLine2;
    private String addrCity;
    private String addrState;
    private String addrZip;

    // Amenities
    private Boolean trashRecyclingCollection;
    private Boolean toilets;
    private Boolean internetConnectivity;
    private Boolean showers;
    private Boolean cellReception;
    private Boolean laundry;
    private Boolean campStore;
    private Boolean staffOnsite;
    private Boolean iceAvailable;
    private Boolean fireWoodAvailable;

    public ParkDetailEntity() {

    }

}
