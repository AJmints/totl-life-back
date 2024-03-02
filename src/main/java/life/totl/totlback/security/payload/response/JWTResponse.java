package life.totl.totlback.security.payload.response;

import life.totl.totlback.users.models.ProfilePictureEntity;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class JWTResponse {

    private String token;
    private long id;
    private String userName;
    private String userEmail;
    private List<String> roles;
    private byte[] userPfp;
    private boolean accountVerified;

    public JWTResponse(String token, long id, String userName, String userEmail, List<String> roles, boolean accountVerified) {
        this.token = token;
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.roles = roles;
        this.accountVerified = accountVerified;
    }
    public JWTResponse(String token, long id, String userName, String userEmail, List<String> roles, byte[] userPfp, boolean accountVerified) {
        this.token = token;
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.roles = roles;
        this.userPfp = userPfp;
        this.accountVerified = accountVerified;
    }
}
