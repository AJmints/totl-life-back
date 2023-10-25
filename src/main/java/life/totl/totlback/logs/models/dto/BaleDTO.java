package life.totl.totlback.logs.models.dto;

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

    public BaleDTO(long id, String parentLog, String title, String body, String userName, byte[] userPFP, long commentCount, long upVoteCount) {
        this.id = id;
        this.parentLog = parentLog;
        this.title = title;
        this.body = body;
        this.userName = userName;
        this.userPFP = userPFP;
        this.commentCount = commentCount;
        this.upVoteCount = upVoteCount;
        // send upvote, downvote, and save to the front, then reset the database. All these fields in previous objects have been set to null, and unless it's a new object with these params, they will break this baleDTO constructor. So we need a clean slate.
    }

}
