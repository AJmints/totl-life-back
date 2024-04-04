package life.totl.totlback.users.models.dtos;

import life.totl.totlback.backpack.models.UserSpecificGearEntity;
import life.totl.totlback.users.models.ProfilePictureEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserContextDTO {
    private String userName;
    private Long userId;
    private Boolean accountVerified;
    private ProfilePictureEntity pfp;
    private List<String> logFollowList;
    private List<String> createdLogs;
    private List<UserSpecificGearEntity> gearItems;

    public UserContextDTO (String userName, Long userId, Boolean accountVerified, List<String> logFollowList, List<String> createdLogs, List<UserSpecificGearEntity> gearItems) {
        this.userName = userName;
        this.userId = userId;
        this.accountVerified = accountVerified;
        this.logFollowList = logFollowList;
        this.createdLogs = createdLogs;
        this.gearItems = gearItems;
    }


}
