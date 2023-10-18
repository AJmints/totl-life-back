package life.totl.totlback.logs.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class LogBalesDTO {
    private String status;
    private String logDescription;
    private List<BaleDTO> allBales;
}
