package life.totl.totlback.logs.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaleEditDTO {

    public long id;
    public String title;
    public String body;
}
