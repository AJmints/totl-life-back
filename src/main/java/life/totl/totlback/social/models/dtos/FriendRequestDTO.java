package life.totl.totlback.social.models.dtos;

import life.totl.totlback.social.models.SocialUserHubEntity;

import java.util.Calendar;

public class FriendRequestDTO {

    private Calendar date;
    private SocialUserHubEntity requester;
    private SocialUserHubEntity requested;
    private String status;


}
