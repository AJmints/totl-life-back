package life.totl.totlback.campevent.models;

import jakarta.persistence.*;
import life.totl.totlback.campevent.models.dtos.responses.InviteListEventResponseDTO;
import life.totl.totlback.logs.models.LogsEntity;
import life.totl.totlback.logs.models.UserLogsBalesEntity;
import life.totl.totlback.social.models.dtos.FriendListDTO;
import life.totl.totlback.users.models.ProfilePictureEntity;
import life.totl.totlback.users.models.UserEntity;
import life.totl.totlback.users.utils.ImageUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "user_campevents")
@AllArgsConstructor
@Getter
@Setter
public class CampEventsRelatedToUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "campEventsRelatedToUser")
    private UserEntity user;

    @ManyToMany
    @Fetch(FetchMode.SELECT)
    @JoinTable(
            name = "campevents_relations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_connections_id"))
     private List<CampEventEntity> relatedCampEvents;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "createBy")
    @Fetch(FetchMode.SELECT)
    private List<CampEventEntity> userMadeCampEvents;

    @ManyToMany(mappedBy = "inviteList")
    @Fetch(FetchMode.SELECT)
    private List<CampEventEntity> memberOfTheseEvents;

    public CampEventsRelatedToUserEntity() {
    }

    public CampEventsRelatedToUserEntity(UserEntity user) {
        this.user = user;
    }

    public InviteListEventResponseDTO getInviteListEventResponseDTO() {
        return new InviteListEventResponseDTO(this.user.getId(), this.user.getUserName(), ImageUtility.decompressImage(this.user.getUserPFP().getImage()), this.user.isAccountVerified(), this.getUserMadeCampEvents().size());
    }

}
