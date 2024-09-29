package life.totl.totlback.social.models.dtos;

import life.totl.totlback.users.models.ProfilePictureEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FriendListDTO {
    private String userName;
    private ProfilePictureEntity pfp;

    public FriendListDTO(String userName) {
        this.userName = userName;
    }
}
