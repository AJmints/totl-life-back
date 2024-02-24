package life.totl.totlback.users.models.dtos;

import life.totl.totlback.users.models.ProfilePictureEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserContextDTO {
    private String userName;
    private Long userId;
    private Boolean accountVerified;
    private ProfilePictureEntity pfp;
}
