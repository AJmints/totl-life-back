package life.totl.totlback.social.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
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
    @Setter
    @ManyToOne
    @JoinColumn(name = "lastActor", referencedColumnName = "id")
    @JsonIgnore
    private SocialUserHubEntity lastActor;
    @Setter
    private String status;  // Pending, friended, unfriended, blocked, declined, canceled, unfriended

    public TurtleRequestEntity() {
    }

    public TurtleRequestEntity(String status, SocialUserHubEntity lastActor, SocialUserHubEntity requested, SocialUserHubEntity requester) {
        this.dateCreated = Calendar.getInstance();
        this.status = status;
        this.lastActor = lastActor;
        this.requested = requested;
        this.requester = requester;
    }

}
