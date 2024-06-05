package life.totl.totlback.users.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseMessage {
    private String status;
    private String response;
    public ResponseMessage(String response) {
        this.response = response;
    }
}
