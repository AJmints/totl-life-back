package life.totl.totlback.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserEntityDTO {

    private String userName;
    private String userEmail;
    private String password;

}
