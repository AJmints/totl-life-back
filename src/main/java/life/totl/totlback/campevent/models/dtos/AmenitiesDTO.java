package life.totl.totlback.campevent.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
public class AmenitiesDTO {
    public String trashRecyclingCollection;
    public ArrayList<String> toilets = new ArrayList<>();
    public String internetConnectivity;
    public ArrayList<String> showers = new ArrayList<>();
    public String cellPhoneReception;
    public String laundry;
    public String amphitheater;
    public String dumpStation;
    public String campStore;
    public String staffOrVolunteerHostOnsite;
    public ArrayList<String> potableWater = new ArrayList<>();
    public String iceAvailableForSale;
    public String firewoodForSale;
    public String foodStorageLockers;
}
