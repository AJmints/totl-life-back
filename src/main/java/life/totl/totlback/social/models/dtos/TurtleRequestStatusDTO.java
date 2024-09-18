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
    public String lastActor;

    public TurtleRequestStatusDTO(String status, String requestStatus, String requester, String requested) {
        this.status = status;
        this.requestStatus = requestStatus;
        this.requester = requester;
        this.requested = requested;
    }
}
