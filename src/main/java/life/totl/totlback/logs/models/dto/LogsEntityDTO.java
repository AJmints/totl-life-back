package life.totl.totlback.logs.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LogsEntityDTO {

    private long user;
    private String logName;
    private String introduction;
}
