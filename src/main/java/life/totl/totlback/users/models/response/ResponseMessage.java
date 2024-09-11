package life.totl.totlback.users.models.response;

import lombok.Getter;

@Getter
public class ResponseMessage {
    private String status;
    private String response;
    public ResponseMessage(String response) {
        this.response = response;
    }
    public ResponseMessage(String status, String response) {
        this.status = status;
        this.response = response;
    }
}
