package life.totl.totlback.social.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TurtleRequestStatusDTO {
    public String status;
    public String requestStatus;
    public String requester;
    public String requested;
}
