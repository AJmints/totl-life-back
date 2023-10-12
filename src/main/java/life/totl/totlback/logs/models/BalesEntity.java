package life.totl.totlback.logs.models;

import jakarta.persistence.*;

@Entity
@Table(name = "bale_forum_post")
public class BalesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private String parentLog;
//    private String userNameOwner;
//    private byte[] userImg;
//    private Long upVoteCount;
//    private Long downVoteCount;
//    private String Comments;
//    private String title;
//    private String preview;


}
