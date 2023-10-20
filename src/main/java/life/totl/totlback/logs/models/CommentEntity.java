package life.totl.totlback.logs.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import life.totl.totlback.logs.models.dto.CommentDTO;
import life.totl.totlback.logs.models.dto.CommentResponseDTO;
import life.totl.totlback.users.models.ProfilePictureEntity;
import life.totl.totlback.users.utils.ImageUtility;
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

//    private List<ChildCommentEntity> childComments = new ArrayList<>();

    private String comment;

    // private Long upVote;
    // private Long downVote;

    public CommentEntity() {
    }

    public CommentEntity(UserLogsBalesEntity commentOwner, BalesEntity parentBale, String comment) {
        this.commentOwner = commentOwner;
        this.parentBale = parentBale;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public UserLogsBalesEntity getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(UserLogsBalesEntity commentOwner) {
        this.commentOwner = commentOwner;
    }

    public BalesEntity getParentBale() {
        return parentBale;
    }

    public void setParentBale(BalesEntity parentBale) {
        this.parentBale = parentBale;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CommentResponseDTO getCommentInformation() {
        return new CommentResponseDTO(
                this.getId(),
                this.getComment(),
                this.parentBale.getId(),
                this.getCommentOwner().getUser().getUserName(),
                ProfilePictureEntity.builder().image(ImageUtility.decompressImage(this.getCommentOwner().getUser().getUserPFP().getImage())).build().getImage());
    }
}