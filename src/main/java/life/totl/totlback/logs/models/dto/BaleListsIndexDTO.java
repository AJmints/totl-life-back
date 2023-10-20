package life.totl.totlback.logs.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BaleListsIndexDTO {
    private int total;
    private List<BaleDTO> baleList;
}
