package life.totl.totlback.users.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginDTO {
    private String userEmail;
    private String userPassword;
}
