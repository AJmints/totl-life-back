package life.totl.totlback.logs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import life.totl.totlback.logs.models.dto.BaleDTO;
import life.totl.totlback.users.models.ProfilePictureEntity;
import life.totl.totlback.users.utils.ImageUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bale_forum_post")
public class BalesEntity implements Serializable {
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

//    private Long downVoteCount;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentBale")
    private final List<CommentEntity> comments = new ArrayList<>();
    @Column(columnDefinition = "VARCHAR(1000) NOT NULL")
    private String title;
    @Column(name = "bale_body", columnDefinition = "VARCHAR(2300) NOT NULL")
    private String body;
    @Column(name = "up_vote")
    private final List<Long> upVoteIds = new ArrayList<>();
    @Column(name = "down_vote")
    private final List<Long> downVoteIds = new ArrayList<>();

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



    public BaleDTO getBaleInformation() {

//        private long id; check
//        private String parentLog; check
//        private String userName; check
//        private byte[] userPFP; check
//        private String title; check
//        private String body; check
//        private long upVoteCount;
//        private long downVoteCount;
//        private long commentCount; check
//        private long saveCount;


        BaleDTO createDTO = new BaleDTO(this.id, this.parentLog.getLogName(), this.title, this.body, baleOwner.getUser().getUserName(), ProfilePictureEntity.builder().image(ImageUtility.decompressImage(this.getBaleOwner().getUser().getUserPFP().getImage())).build().getImage(), this.getComments().size(), this.getUpVoteIds().size());

        return createDTO;
    }
}
