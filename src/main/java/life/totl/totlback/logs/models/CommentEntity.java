package life.totl.totlback.logs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "comment")
@Getter
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "comment_owner", referencedColumnName = "id")
    @JsonIgnore
    private UserLogsBalesEntity commentOwner;

    @ManyToOne
    @JoinColumn(name = "parent_bale", referencedColumnName = "id")
    @JsonIgnore
    private BalesEntity parentBale;

    public CommentEntity() {
    }
}
