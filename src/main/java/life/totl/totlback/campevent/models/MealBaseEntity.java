package life.totl.totlback.campevent.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "base_meal_obj")
@AllArgsConstructor
@Getter
@Setter
public class MealBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mealEntry")
    private List<MealRecEntity> mealRecEntities;

    private String bfast;
    private String lunch;
    private String dinner;
    private String snacks;

    public MealBaseEntity() {

    }

}
