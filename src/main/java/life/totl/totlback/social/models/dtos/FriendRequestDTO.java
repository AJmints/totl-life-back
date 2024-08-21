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
    private SocialUserHubEntity requester;
    private SocialUserHubEntity requested;
    private String status;
    // setup


    public FriendRequestDTO(SocialUserHubEntity requester, SocialUserHubEntity requested, String status) {
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

    public SocialUserHubEntity getRequester() {
        return requester;
    }

    public void setRequester(SocialUserHubEntity requester) {
        this.requester = requester;
    }

    public SocialUserHubEntity getRequested() {
        return requested;
    }

    public void setRequested(SocialUserHubEntity requested) {
        this.requested = requested;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
