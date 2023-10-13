package life.totl.totlback.users.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import life.totl.totlback.logs.models.UserLogsBalesEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
//    @JoinTable(name = "user_logs_connection",
//    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//    inverseJoinColumns = @JoinColumn(name = "logs_bales_id", referencedColumnName = "id"))
    @JoinColumn(name = "user_forums_ids", referencedColumnName = "id")
    private UserLogsBalesEntity userLogsBalesEntity;

    public UserEntity(String userName, String userEmail, String pwHash, boolean accountVerified) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.pwHash = encoder.encode(pwHash);
        this.accountVerified = accountVerified;
        this.userLogsBalesEntity = new UserLogsBalesEntity(this);
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

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
}
