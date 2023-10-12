package life.totl.totlback.logs.models;

import jakarta.persistence.*;
import life.totl.totlback.users.models.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "logs_subjects")
@Getter
@Setter
public class LogsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "log_owner", referencedColumnName = "id")
    private UserLogsBalesEntity logOwner;
    private String logName;
    private String logDescription;

    public LogsEntity(UserLogsBalesEntity logOwner, String logName, String logDescription) {
        this.logOwner = logOwner;
        this.logName = logName;
        this.logDescription = logDescription;
    }

    public LogsEntity() {
    }
}
