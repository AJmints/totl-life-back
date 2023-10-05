package life.totl.totlback.security.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class JWTResponse {

    private String token;
    private long id;
    private String userName;
    private String userEmail;
    private List<String> roles;
    private boolean accountVerified;

    public JWTResponse(String token, long id, String userName, String userEmail, List<String> roles, boolean accountVerified) {
        this.token = "Bearer " + token;
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.roles = roles;
        this.accountVerified = accountVerified;
    }
}
