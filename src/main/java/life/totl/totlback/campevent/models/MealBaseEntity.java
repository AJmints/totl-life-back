package life.totl.totlback.campevent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import life.totl.totlback.campevent.models.dtos.FoodRecDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

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
    @JsonIgnore
    private List<MealRecEntity> mealRecEntities;

    private String bfast;
    private String lunch;
    private String dinner;
    private String snacks;

    public MealBaseEntity() {

    }

    public MealBaseEntity(String snacks, String dinner, String lunch, String bfast) {
        this.snacks = snacks;
        this.dinner = dinner;
        this.lunch = lunch;
        this.bfast = bfast;
    }

    public boolean SameBaseMeal(FoodRecDTO meal) {
        if (Objects.equals(this.bfast, meal.bfast) && Objects.equals(this.lunch, meal.lunch) && Objects.equals(this.dinner, meal.dinner) && Objects.equals(this.snacks, meal.snacks)) {
            return true;
        } else {
            return false;
        }
    }
}
