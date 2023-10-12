package life.totl.totlback.logs.models;

import jakarta.persistence.*;
import life.totl.totlback.users.models.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_logs_bales") // rename logs_bales_by_user
@Getter
@Setter
public class UserLogsBalesEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(mappedBy = "userLogsBalesEntity")
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "logOwner")
    private List<LogsEntity> logsEntities = new ArrayList<>();

    public UserLogsBalesEntity(UserEntity user) {
        this.user = user;
    }

    public UserLogsBalesEntity() {
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
