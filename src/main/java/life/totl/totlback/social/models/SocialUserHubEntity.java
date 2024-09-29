package life.totl.totlback.social.models;

import jakarta.persistence.*;
import life.totl.totlback.social.models.dtos.FriendListDTO;
import life.totl.totlback.social.models.dtos.UserTurtleRequestActionDTO;
import life.totl.totlback.users.models.ProfilePictureEntity;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.utils.ImageUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "user_social_hub")
public class SocialUserHubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "socialHub")
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requested")
    private List<TurtleRequestEntity> requested;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requester")
    private List<TurtleRequestEntity> requester;
    @ManyToMany
    @JoinTable(
            name = "all_friend_connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "social_hub_id"))
    private List<UserEntity> friendList;

    // followList

    // directMessage

    public SocialUserHubEntity(UserEntity user) {
        this.user = user;
        this.friendList = new ArrayList<>();
    }

    public SocialUserHubEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public List<FriendListDTO> getUsersFriendList() {
        List<FriendListDTO> userFriends = new ArrayList<>();
        for (UserEntity friends : this.friendList) {
            FriendListDTO friendListDTO;
            if (Arrays.equals(friends.getUserPFP().getImage(), new byte[256])) {
                friendListDTO = new FriendListDTO(friends.getUserName());
            } else {
                friendListDTO = new FriendListDTO(friends.getUserName(), ProfilePictureEntity.builder().image(ImageUtility.decompressImage(friends.getUserPFP().getImage())).build());
            }
            userFriends.add(friendListDTO);
        }

        return userFriends;
    }

    public List<UserEntity> getFriendList() {return friendList;}

    public void setFriendList(List<UserEntity> friendList) {
        this.friendList = friendList;
    }
}
