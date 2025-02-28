package life.totl.totlback.campevent.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AddressDTO {
    public String postalCode;
    public String city;
    public String stateCode;
    public String countryCode;
    public String provinceTerritoryCode;
    public String line1;
    public String type;
    public String line2;
    public String line3;
}
