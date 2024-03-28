package life.totl.totlback.backpack.models;

import jakarta.persistence.*;
import life.totl.totlback.users.models.UserEntity;

@Entity
@Table(name = "user_backpack")
public class BackPackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(mappedBy = "userBackPack")
    private UserEntity user;

    /** A configuration will be a camping/floating/backpacking version and each will represent the gear for each scenario */
    //private List<BackPackConfiguration> backPackConfig

    /** This list holds a class that is the GearItem and has user specific notes about the item. This class is made so GearItem can be reused for other users while isolating the user specific details */
    //private List<GearItem + GearUserNotes = UserGearItem> userGear

    public BackPackEntity(UserEntity user) { this.user = user; }

    public BackPackEntity() {}

    public long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
