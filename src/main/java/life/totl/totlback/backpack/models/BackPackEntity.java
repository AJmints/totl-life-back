package life.totl.totlback.backpack.models;

import jakarta.persistence.*;
import life.totl.totlback.users.models.UserEntity;

import java.util.List;

@Entity
@Table(name = "user_backpack")
public class BackPackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(mappedBy = "userBackPack")
    private UserEntity user;

    /** A configuration will be a camping/floating/backpacking version and each will represent the gear for each scenario */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentBackPack")
    private List<BackPackConfigurationEntity> backPackConfig;

    /** This list holds a class that is the GearItem and has user specific notes about the item. This class is made so GearItem can be reused for other users while isolating the user specific details */
    /** Experiment with set, it will be faster to iterate through and might benefit loading times. */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userBackPack")
    private List<UserSpecificGearEntity> userGear;

    public BackPackEntity() {
    }

    public BackPackEntity(UserEntity user) { this.user = user; }

    public long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<BackPackConfigurationEntity> getBackPackConfig() {
        return backPackConfig;
    }

    public void setBackPackConfig(List<BackPackConfigurationEntity> backPackConfig) {
        this.backPackConfig = backPackConfig;
    }

    public List<UserSpecificGearEntity> getUserGear() {
        return userGear;
    }

    public void setUserGear(List<UserSpecificGearEntity> userGear) {
        this.userGear = userGear;
    }
}
