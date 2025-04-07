package life.totl.totlback.campevent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import life.totl.totlback.backpack.models.BackPackEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "day_meal_rec")
@AllArgsConstructor
@Setter
@Getter
public class MealRecEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @JsonIgnore
    private CampEventEntity eventMealRec;

    @ManyToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "base_meal_obj_id", referencedColumnName = "id")
    private MealBaseEntity mealEntry;

    private Integer sequenceId;
    private String userNotes;


    public MealRecEntity() {
    }

    public MealRecEntity(MealBaseEntity mealBaseEntity, String userNotes, Integer sequenceId) {
        this.mealEntry = mealBaseEntity;
        this.userNotes = userNotes;
        this.sequenceId = sequenceId;
    }
}
