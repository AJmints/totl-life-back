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

    public UserContextDTO (String userName, Long userId, Boolean accountVerified) {
        this.userName = userName;
        this.userId = userId;
        this.accountVerified = accountVerified;
    }
}
