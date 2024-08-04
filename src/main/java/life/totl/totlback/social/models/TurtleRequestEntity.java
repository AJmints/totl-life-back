package life.totl.totlback.social.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Calendar;

@Entity
@Table(name = "turtle_request_entities")
public class TurtleRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Calendar dateCreated;
    @ManyToOne
    @JoinColumn(name = "requester", referencedColumnName = "id")
    @JsonIgnore
    private SocialUserHubEntity requester;
    @ManyToOne
    @JoinColumn(name = "requested", referencedColumnName = "id")
    @JsonIgnore
    private SocialUserHubEntity requested;
    private String status;  // Pending, accepted, declined, canceled, unfriended

    public TurtleRequestEntity() {
    }

    public TurtleRequestEntity(SocialUserHubEntity requester, SocialUserHubEntity requested, String status) {
        this.dateCreated = Calendar.getInstance();
        this.requester = requester;
        this.requested = requested;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public SocialUserHubEntity getRequester() {
        return requester;
    }

    public SocialUserHubEntity getRequested() {
        return requested;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
