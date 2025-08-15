package life.totl.totlback.campevent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import life.totl.totlback.backpack.models.GearItemsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_rec")
@AllArgsConstructor
@Getter
@Setter
public class ItemRecEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @JsonIgnore
    private CampEventEntity eventGearRec;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gear_item_id", referencedColumnName = "id")
    @JsonIgnore
    private GearItemsEntity gearRec; // Handles Gear Type and Category

    private String groupType;
    private Integer count;

    public ItemRecEntity() {

    }

    public ItemRecEntity(GearItemsEntity gearRec, String groupType, Integer count) {
        this.gearRec = gearRec;
        this.groupType = groupType;
        this.count = count;
    }

    @PreRemove
    private void removeItemRecEntity() {this.gearRec.removeGearItemsEntity(this);}
}
