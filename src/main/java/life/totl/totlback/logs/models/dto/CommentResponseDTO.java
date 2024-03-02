package life.totl.totlback.logs.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentResponseDTO {
    private Long id;
    private String comment;
    private Long parentBaleId;
    private String userName;
    private byte[] userPFP;

    public CommentResponseDTO(Long id, String comment, Long parentBaleId, String userName) {
        this.id = id;
        this.comment = comment;
        this.parentBaleId = parentBaleId;
        this.userName = userName;
    }
}
