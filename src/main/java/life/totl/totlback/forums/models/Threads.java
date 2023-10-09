package life.totl.totlback.forums.models;

import jakarta.persistence.*;

@Entity
@Table(name = "threads")
public class Threads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

}
