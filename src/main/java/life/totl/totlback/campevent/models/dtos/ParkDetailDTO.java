package life.totl.totlback.campevent.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ParkDetailDTO {

    public AddressDTO address;
    public String addressString;
    public AmenitiesDTO amenities;
    public String id;
    public String latitude;
    public String longitude;
    public String name;
    public String url;

}
