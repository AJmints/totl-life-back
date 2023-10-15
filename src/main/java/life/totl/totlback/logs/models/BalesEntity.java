package life.totl.totlback.logs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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
//    private byte[] userImg;
//    private Long upVoteCount;
//    private Long downVoteCount;
//    private CommentEntity Comments;
    private String title;
    @Column(name = "bale_body", columnDefinition = "VARCHAR(2300) NOT NULL")
    private String body;

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
}
