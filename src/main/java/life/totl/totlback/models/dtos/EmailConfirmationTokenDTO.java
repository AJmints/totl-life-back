package life.totl.totlback.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class EmailConfirmationTokenDTO {
    private String tokenId;
}
