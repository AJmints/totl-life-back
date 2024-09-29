package life.totl.totlback.social.models.dtos;
import life.totl.totlback.users.models.ProfilePictureEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserTurtleRequestActionDTO {

        private String requester;
        private String requested;
        private ProfilePictureEntity userPFP;

        public UserTurtleRequestActionDTO(String requester, String requested) {
            this.requester = requester;
            this.requested = requested;
        }


}
