package life.totl.totlback.logs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    @JsonIgnore
    private UserLogsBalesEntity logOwner;
    @Size(min = 3, max = 15)
    private String logName;
    @Column(name = "log_description", columnDefinition = "VARCHAR(2300) NOT NULL")
    @Size(min = 40, max = 500)
    private String logDescription;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentLog")
    private final List<BalesEntity> allLogBales = new ArrayList<>();

    public LogsEntity(UserLogsBalesEntity logOwner, String logName, String logDescription) {
        this.logOwner = logOwner;
        this.logName = logName;
        this.logDescription = logDescription;
    }

    public LogsEntity() {
    }
}
