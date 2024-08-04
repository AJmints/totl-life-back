package life.totl.totlback.social.models;

import jakarta.persistence.*;
import life.totl.totlback.users.models.UserEntity;

import java.util.ArrayList;
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

    public List<UserEntity> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<UserEntity> friendList) {
        this.friendList = friendList;
    }
}
