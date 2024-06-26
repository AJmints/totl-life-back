package life.totl.totlback.backpack.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "pack_config_user")
public class BackPackConfigurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "parent_back_pack", referencedColumnName = "id")
    @JsonIgnore
    private BackPackEntity parentBackPack;
    @ManyToMany
    @JoinTable(
            name = "config_pack_contents",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "pack_config_id"))
    private List<UserSpecificGearEntity> userGearList;
    private Calendar dateCreated;
    private String configType; // hiking pack / car camp pack / float pack
    private String packName;
    private boolean hidden;

    public BackPackConfigurationEntity() {
    }

    public BackPackConfigurationEntity(BackPackEntity backPackEntity, String configType, String packName, List<UserSpecificGearEntity> userGearList) {
        this.dateCreated = Calendar.getInstance();
        this.parentBackPack = backPackEntity;
        this.configType = configType;
        this.packName = packName;
        this.userGearList = userGearList;
        this.hidden = false;
    }

    public long getId() {
        return id;
    }

    public BackPackEntity getParentBackPack() {
        return parentBackPack;
    }

    public String getConfigType() {
        return configType;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }


    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public List<UserSpecificGearEntity> getUserGearList() {
        return userGearList;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }


}
