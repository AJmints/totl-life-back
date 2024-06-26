package life.totl.totlback.backpack.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "user_specific_gear")
public class UserSpecificGearEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gear_item_id", referencedColumnName = "id")
    private GearItemsEntity gearItem;

    @ManyToOne
    @JoinColumn(name = "user_backpack", referencedColumnName = "id")
    @JsonIgnore
    private BackPackEntity userBackPack;
    @ManyToMany(mappedBy = "userGearList")
    private List<BackPackConfigurationEntity> configBackPacks;
    private Calendar dateCreated;
    @Column(columnDefinition = "VARCHAR(255)")
    private String additionalDetails;
    private boolean lendable;
    private int quantity;
    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String itemCondition; //Good / bad / used / rough / broken
    private boolean forSale;
    private double price;
    private boolean hidden;

    public UserSpecificGearEntity() {
    }

    public UserSpecificGearEntity(BackPackEntity userBackPack, GearItemsEntity gearItem, String additionalDetails, boolean lendable, int quantity, String itemCondition, boolean hidden, boolean forSale, double price) {
        this.dateCreated = Calendar.getInstance();
        this.gearItem = gearItem;
        this.userBackPack = userBackPack;
        this.additionalDetails = additionalDetails;
        this.lendable = lendable;
        this.quantity = quantity;
        this.itemCondition = itemCondition;
        this.hidden = hidden;
        this.forSale = forSale;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public GearItemsEntity getGearItem() {
        return gearItem;
    }

    public void setGearItem(GearItemsEntity gearItem) {
        this.gearItem = gearItem;
    }

    public BackPackEntity getUserBackPack() {
        return userBackPack;
    }


    public Calendar getDateCreated() {
        return dateCreated;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public boolean isLendable() {
        return lendable;
    }

    public void setLendable(boolean lendable) {
        this.lendable = lendable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isForSale() {
        return forSale;
    }

    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @PreRemove
    private void removeGearItemsEntity() {
        this.gearItem.removeGearItemsEntity(this);
    }
}


