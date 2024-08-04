package life.totl.totlback.users.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import life.totl.totlback.backpack.models.BackPackEntity;
import life.totl.totlback.logs.models.LogsEntity;
import life.totl.totlback.logs.models.UserLogsBalesEntity;
import life.totl.totlback.social.models.SocialUserHubEntity;
import life.totl.totlback.social.models.TurtleRequestEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 3, max = 30)
    private String userName;

    @NotEmpty
    @NotNull
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String userEmail;

    @NotNull
    @NotEmpty
    private String pwHash;
    private boolean accountVerified;

    private List<String> roles = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_forums_ids", referencedColumnName = "id")
    private UserLogsBalesEntity userLogsBalesEntity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_pfp_id", referencedColumnName = "id")
    private ProfilePictureEntity userPFP;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_backpack", referencedColumnName = "id")
    private BackPackEntity userBackPack;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_social_hub", referencedColumnName = "id")
    private SocialUserHubEntity socialHub;

    @ManyToMany(mappedBy = "friendList")
    private List<SocialUserHubEntity> friendConnections;

    public UserEntity(String userName, String userEmail, String pwHash, boolean accountVerified) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.pwHash = encoder.encode(pwHash);
        this.accountVerified = accountVerified;
        this.userLogsBalesEntity = new UserLogsBalesEntity(this);
        //The way PFP was set up is a flaw that will be rebuilt later or fixed in new version. This is a learning project.
        this.userPFP = new ProfilePictureEntity();
        this.userBackPack = new BackPackEntity(this);
        this.socialHub = new SocialUserHubEntity(this);
    }

    public SocialUserHubEntity getSocialHub() {
        return socialHub;
    }

    public void setSocialHub(SocialUserHubEntity socialHub) {
        this.socialHub = socialHub;
    }

    public UserEntity() {
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserLogsBalesEntity(UserLogsBalesEntity userLogsBalesEntity) {
        this.userLogsBalesEntity = userLogsBalesEntity;
    }

    public UserLogsBalesEntity getUserLogsBalesEntity() {
        return userLogsBalesEntity;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    public boolean isAccountVerified() {
        return accountVerified;
    }

    public void setAccountVerified(boolean accountVerified) {
        this.accountVerified = accountVerified;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getIdString() { return String.valueOf(this.id); }

    public ProfilePictureEntity getUserPFP() {
        return userPFP;
    }

    public void setUserPFP(ProfilePictureEntity userPFP) {
        this.userPFP = userPFP;
    }

    public BackPackEntity getUserBackPack() {
        return userBackPack;
    }

    public void setUserBackPack(BackPackEntity userBackPack) {
        this.userBackPack = userBackPack;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public List<String> getUserMadeLogs() {
        List<String> userMadeLogs = new ArrayList<>();
        for (LogsEntity logs : this.userLogsBalesEntity.getLogsEntities()) {
            if (logs.getLogOwner().getUser() == this) {
                userMadeLogs.add(logs.getLogName());
            }
        }
        return userMadeLogs;
    }

    public List<Long> getFollowingLogs() {
        List<Long> followingLogs = new ArrayList<>();
        for (Long logId : this.userLogsBalesEntity.getLogFollow()) {
            followingLogs.add(logId);
        }
        return followingLogs;
    }
}
