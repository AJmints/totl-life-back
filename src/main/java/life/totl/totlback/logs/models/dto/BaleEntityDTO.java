package life.totl.totlback.logs.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaleEntityDTO {
    private long userId;
    private String parentLog;
    private String title;
    private String body;
}
