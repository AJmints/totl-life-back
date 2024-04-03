package life.totl.totlback.backpack.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import life.totl.totlback.backpack.models.dtos.GearItemDTO;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gear_item")
public class GearItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gearItem")
    private List<UserSpecificGearEntity> userLink;
    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String category; // tent / hammock / sleeping bag / sleeping pad / fridge / cooler / solar panel / battery / tool / shower / stove / tarp / cookware / water filter / clothing / consumable / seating / hammock / backpack / Water Proof Dry Bag
    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String brand; // Osprey / Kelty / BlackDiamond / Sawyer / Katadyn / AmazonBrand / Coleman / REI / North Face / Solomon / CamelBack /
    @Column(columnDefinition = "VARCHAR(64) NOT NULL")
    @Size(min = 0, max = 30)
    private String model; //Specific name of item, optional
    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String type; // Knife / pot / pan / spoon / Jacket / Pants / Boots / Flashlight / axe / saw / lighter / cordage / chair / table / game / headlamp / fire starter / shovel / first aide / hand sanitation / dish soap / bug spray / sunscreen / games / Water Container / daypack / hiking pack / hydration pack
    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String rating; // 50spf / 3season / 20degrees / 20%deet
    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String storage; // 40L / 500watts / 7gal
    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String powerSource; // battery / solar / pump / squeeze / propane / manual
    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String size; // 2person / 6person / Large / small / 10oz
    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String extraInfo; // rainfly / usb-c / micro-usb / usb-a / mini-usb /
    private double weight;
    private double height;
    private double width;
    private double length;
    private double userScore; // scale of 1-5 stars

    public GearItemsEntity() {
    }

    public GearItemsEntity(String category, String brand, String type, String extraInfo, String model, String size, String storage, double weight) {
        /** BackPack Constructor */
        this.category = category;
        this.brand = brand;
        this.type = type;
        this.extraInfo = extraInfo;
        this.model = model;
        this.size = size;
        this.storage = storage;
        this.weight = weight;
        this.powerSource = "";
        this.rating = "";
    }

    public long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getPowerSource() {
        return powerSource;
    }

    public void setPowerSource(String powerSource) {
        this.powerSource = powerSource;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getUserScore() {
        return userScore;
    }

    public void setUserScore(double userScore) {
        this.userScore = userScore;
    }

    public boolean equalBackPack(GearItemDTO gear) {
        if (Objects.equals(this.brand, gear.getBrand()) && Objects.equals(this.category, gear.getCategory()) && Objects.equals(this.extraInfo, gear.getExtraInfo()) && Objects.equals(this.model, gear.getModel()) && Objects.equals(this.size, gear.getSize()) && Objects.equals(this.storage, gear.getStorage()) && Objects.equals(this.type, gear.getType()) && Objects.equals(this.weight, gear.getWeight())) {
            return true;
        } else {
            return false;
        }
    }
}
