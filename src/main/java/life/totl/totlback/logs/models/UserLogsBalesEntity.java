package life.totl.totlback.logs.models;

import jakarta.persistence.*;
import life.totl.totlback.users.models.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "logs_bales_by_user")
public class UserLogsBalesEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(mappedBy = "userLogsBalesEntity")
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "logOwner")
    private final List<LogsEntity> logsEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "baleOwner")
    private final List<BalesEntity> baleEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "commentOwner")
    private final List<CommentEntity> commentEntities = new ArrayList<>();
    private final List<Long> logFollow = new ArrayList<>();

    public UserLogsBalesEntity(UserEntity user) {
        this.user = user;
    }

    public UserLogsBalesEntity() {
    }

    public long getId() {
        return id;
    }

    public List<LogsEntity> getLogsEntities() {
        return logsEntities;
    }

    public UserEntity getUser() {
        return user;
    }

    public List<BalesEntity> getBaleEntities() {
        return baleEntities;
    }

    public List<CommentEntity> getCommentEntities() {
        return commentEntities;
    }

    public List<Long> getLogFollow() {
        return logFollow;
    }
}
