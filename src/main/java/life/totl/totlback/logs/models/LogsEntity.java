package life.totl.totlback.logs.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "logs_on_pond")
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
    @Column(name = "log_description", columnDefinition = "VARCHAR(2300) NOT NULL")
    private String logDescription;

    public LogsEntity(UserLogsBalesEntity logOwner, String logName, String logDescription) {
        this.logOwner = logOwner;
        this.logName = logName;
        this.logDescription = logDescription;
    }

    public LogsEntity() {
    }
}
