package life.totl.totlback.logs.models.dto;

import life.totl.totlback.logs.models.BalesEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class LogBalesDTO {
    private String status;
    private List<BaleDTO> allBales;
}
