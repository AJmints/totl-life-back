package life.totl.totlback.social.models.dtos;

import life.totl.totlback.social.models.SocialUserHubEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class FriendRequestDTO {

    private Calendar date;
    private String requester;
    private String requested;
    private String status;

    public FriendRequestDTO(String requester, String requested, String status) {
        this.date = Calendar.getInstance();
        this.requester = requester;
        this.requested = requested;
        this.status = status;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getRequested() {
        return requested;
    }

    public void setRequested(String requested) {
        this.requested = requested;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
