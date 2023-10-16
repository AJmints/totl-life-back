package life.totl.totlback.users.models.response;

import life.totl.totlback.users.models.ProfilePictureEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserProfileInfo {
    private ProfilePictureEntity pfp;
    private String userName;
}
