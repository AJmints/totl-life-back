package life.totl.totlback.campevent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import life.totl.totlback.backpack.models.BackPackEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "day_meal_rec")
@AllArgsConstructor
@Setter
@Getter
public class MealRecEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @JsonIgnore
    private CampEventEntity eventMealRec;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "base_meal_obj_id", referencedColumnName = "id")
    private MealBaseEntity mealEntry;

    private String userNotes;

    public MealRecEntity() {
    }
}
