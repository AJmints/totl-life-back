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

    /** Lists of owned items by user, can add to various backpack configs */
    //private List<CookWareItems> CookWare

    //private List<BaseGearItems> BaseGear

    //private List<ClothingGearItems> clothingItems

    //private List<Tools> tools

    //portable battery
    //solar panels
    //cooler / portable fridge
    //tent / hammock
    //backpack
    //sleeping bag / sleeping pad
    //stove
    //shower
    //tarp
    //cookware
    //water filter
    //clothing
    //tools - flashlight / headlamp / knife / axe / saw / cordage / fire starters / shovel
    //first aide / sanitation
    //chair / table
    //games

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
