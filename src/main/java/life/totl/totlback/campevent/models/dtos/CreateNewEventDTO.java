package life.totl.totlback.campevent.models.dtos;

import life.totl.totlback.users.models.UserEntity;

import java.util.Date;
import java.util.List;

public class CreateNewEventDTO {

    private String eventName;
    private Boolean isPrivate;
    private String startDate;
    private String startTime;
    private Date eventStart;
    private String endDate;
    private String endTime;
    private Date eventEnd;
    private String eventDetails;

    private UserEntity createBy;
    private Date createDate;

    private String state;
    private String parkName;
    private String parkAddress;

    private ParkDetailDTO campGround;
    private List<ItemRecDTO> gearRecItems;
    private List<FoodRecDTO> foodRecList;
    private List<UserEntity> inviteList;

}
