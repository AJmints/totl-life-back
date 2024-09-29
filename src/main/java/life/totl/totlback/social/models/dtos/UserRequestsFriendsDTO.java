package life.totl.totlback.social.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserRequestsFriendsDTO {
    List<FriendListDTO> friendList;
    List<UserTurtleRequestActionDTO> turtleRequest;

    public UserRequestsFriendsDTO(List<FriendListDTO> friendList) {
        this.friendList = friendList;
    }
}
