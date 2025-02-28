package life.totl.totlback.campevent.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "park_detail")
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
    private String trashRecyclingCollection;
    private Boolean toilets;
    private String internetConnectivity;
    private Boolean showers;
    private String cellReception;
    private String laundry;
    private String campStore;
    private String staffOnsite;
    private String iceAvailable;
    private String fireWoodAvailable;

    public ParkDetailEntity() {

    }

    public ParkDetailEntity(String parkName, String addressString, String parkGovID, String parkURL, String latitude, String longitude, String addrLine1, String addrLine2, String addrCity, String addrState, String addrZip, String trashRecyclingCollection, Boolean toilets, String internetConnectivity, Boolean showers, String cellReception, String laundry, String campStore, String staffOnsite, String iceAvailable, String fireWoodAvailable) {
        this.parkName = parkName;
        this.addressString = addressString;
        this.parkGovID = parkGovID;
        this.parkURL = parkURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addrLine1 = addrLine1;
        this.addrLine2 = addrLine2;
        this.addrCity = addrCity;
        this.addrState = addrState;
        this.addrZip = addrZip;
        this.trashRecyclingCollection = trashRecyclingCollection;
        this.toilets = toilets;
        this.internetConnectivity = internetConnectivity;
        this.showers = showers;
        this.cellReception = cellReception;
        this.laundry = laundry;
        this.campStore = campStore;
        this.staffOnsite = staffOnsite;
        this.iceAvailable = iceAvailable;
        this.fireWoodAvailable = fireWoodAvailable;
    }
}
