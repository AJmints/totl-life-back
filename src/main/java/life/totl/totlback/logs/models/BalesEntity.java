package life.totl.totlback.logs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import life.totl.totlback.logs.models.dto.BaleDTO;
import life.totl.totlback.users.models.ProfilePictureEntity;
import life.totl.totlback.users.utils.ImageUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "bale_forum_post")
public class BalesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "parent_log", referencedColumnName = "id")
    @JsonIgnore
    private LogsEntity parentLog;
    @ManyToOne
    @JoinColumn(name = "bale_owner", referencedColumnName = "id")
    @JsonIgnore
    private UserLogsBalesEntity baleOwner;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentBale")
    private final List<CommentEntity> comments = new ArrayList<>();
    @Column(columnDefinition = "VARCHAR(1000) NOT NULL")
    @Size(min = 10, max = 150)
    private String title;
    @Column(name = "bale_body", columnDefinition = "VARCHAR(2300) NOT NULL")
    @Size(min = 10, max = 600)
    private String body;
    @Column(name = "up_vote")
    private final List<Long> upVoteIds = new ArrayList<>();
    @Column(name = "down_vote")
    private final List<Long> downVoteIds = new ArrayList<>();

    @Column(name = "edited")
    @NotNull
    private boolean edited;

    public BalesEntity(LogsEntity parentLog, UserLogsBalesEntity baleOwner, String title, String body) {
        this.parentLog = parentLog;
        this.baleOwner = baleOwner;
        this.title = title;
        this.body = body;
    }

    public BalesEntity() {
    }

    public long getId() {
        return id;
    }

    public LogsEntity getParentLog() {
        return parentLog;
    }

    public UserLogsBalesEntity getBaleOwner() {
        return baleOwner;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public List<Long> getUpVoteIds() {
        return upVoteIds;
    }

    public List<Long> getDownVoteIds() {
        return downVoteIds;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public BaleDTO getBaleInformation() {

        if (Arrays.equals(this.baleOwner.getUser().getUserPFP().getImage(), new byte[256])) {
            return new BaleDTO(this.id, this.parentLog.getLogName(), this.title, this.body, baleOwner.getUser().getUserName(),this.getComments().size(), this.getUpVoteIds().size(), this.getDownVoteIds().size(), this.isEdited());
        } else {
            return new BaleDTO(this.id, this.parentLog.getLogName(), this.title, this.body, baleOwner.getUser().getUserName(), ProfilePictureEntity.builder().image(ImageUtility.decompressImage(this.getBaleOwner().getUser().getUserPFP().getImage())).build().getImage(), this.getComments().size(), this.getUpVoteIds().size(), this.getDownVoteIds().size(), this.isEdited());
        }

    }

    public BaleDTO getBalePostPageView() {

        if (Arrays.equals(this.baleOwner.getUser().getUserPFP().getImage(), new byte[256])) {
            return new BaleDTO(this.id, this.parentLog.getLogName(), this.title, this.body, baleOwner.getUser().getUserName(),this.upVoteIds, this.downVoteIds, this.edited);
        } else {
            return new BaleDTO(this.id, this.parentLog.getLogName(), this.title, this.body, baleOwner.getUser().getUserName(), ProfilePictureEntity.builder().image(ImageUtility.decompressImage(this.getBaleOwner().getUser().getUserPFP().getImage())).build().getImage(), this.upVoteIds, this.downVoteIds, this.isEdited());
        }

    }

    @PreRemove
    private void removeBaleEntityFromParentEntities() {
        this.parentLog.removeBaleFromLogEntity(this);
        this.baleOwner.removeBaleFromLogEntity(this);
    }
}
