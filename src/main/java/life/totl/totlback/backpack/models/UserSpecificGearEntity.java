package life.totl.totlback.backpack.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_specific_gear")
public class UserSpecificGearEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "gear_item_id", referencedColumnName = "id")
    private GearItemsEntity gearItem;
    private String additionalDetails;
    private boolean lendable;
    private String itemCondition; //Good / bad / used / rough / broken

    public UserSpecificGearEntity() {
    }
}


