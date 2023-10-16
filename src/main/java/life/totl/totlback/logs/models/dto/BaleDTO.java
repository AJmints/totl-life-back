package life.totl.totlback.logs.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import life.totl.totlback.logs.models.LogsEntity;
import life.totl.totlback.logs.models.UserLogsBalesEntity;
import life.totl.totlback.users.models.ProfilePictureEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaleDTO {

    private long id;
    private String parentLog;
    private String userName;
    private byte[] userPFP;
    private String title;
    private String body;
    private long upVoteCount;
    private long downVoteCount;
    private long commentCount;
    private long saveCount;

    public BaleDTO(long id, String parentLog, String title, String body, String userName, byte[] userPFP) {
        this.id = id;
        this.parentLog = parentLog;
        this.title = title;
        this.body = body;
        this.userName = userName;
        this.userPFP = userPFP;
    }

}
