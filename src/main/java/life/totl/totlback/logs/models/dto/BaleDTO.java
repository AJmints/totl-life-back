package life.totl.totlback.logs.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private List<Long> upVoteIds = new ArrayList<>();
    private List<Long> downVoteIds = new ArrayList<>();
    private boolean edited;

    public BaleDTO(long id, String parentLog, String title, String body, String userName, byte[] userPFP, long commentCount, long upVoteCount, long downVoteCount, boolean edited) {
        this.id = id;
        this.parentLog = parentLog;
        this.title = title;
        this.body = body;
        this.userName = userName;
        this.userPFP = userPFP;
        this.commentCount = commentCount;
        this.upVoteCount = upVoteCount;
        this.downVoteCount = downVoteCount;
        this.edited = edited;

    }

    public BaleDTO(long id, String parentLog, String title, String body, String userName, byte[] userPFP, List<Long> upVotes, List<Long> downVotes, boolean edited) {
        this.id = id;
        this.parentLog = parentLog;
        this.title = title;
        this.body = body;
        this.userName = userName;
        this.userPFP = userPFP;
        this.upVoteIds = upVotes;
        this.downVoteIds = downVotes;
        this.edited = edited;
    }

    public BaleDTO(long id, String parentLog, String title, String body, String userName, long commentCount, long upVotes, long downVotes, boolean edited) {
        this.id = id;
        this.parentLog = parentLog;
        this.title = title;
        this.body = body;
        this.userName = userName;
        this.commentCount = commentCount;
        this.upVoteCount = upVotes;
        this.downVoteCount = downVotes;
        this.edited = edited;
    }

    public BaleDTO(long id, String parentLog, String title, String body, String userName, List<Long> upVotes, List<Long> downVotes, boolean edited) {
        this.id = id;
        this.parentLog = parentLog;
        this.title = title;
        this.body = body;
        this.userName = userName;
        this.upVoteIds = upVotes;
        this.downVoteIds = downVotes;
        this.edited = edited;
    }

}
