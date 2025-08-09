package life.totl.totlback.campevent.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InviteListEventResponseDTO {
    // define in UserEntity, or CampEventsRelatedToUserEntity a method that would make this class for List use.

    private Long id;
    private String userName;
    private byte[] userPFP;
    private Boolean isVerified;
    private int ownedPacksQty;

}
