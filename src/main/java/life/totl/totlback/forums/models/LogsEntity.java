package life.totl.totlback.forums.models;

import jakarta.persistence.*;

@Entity
@Table(name = "logs")
public class LogsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
