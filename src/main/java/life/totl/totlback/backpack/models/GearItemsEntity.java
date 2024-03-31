package life.totl.totlback.backpack.models;

import jakarta.persistence.*;

@Entity
@Table(name = "gear_item")
public class GearItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "gearItem")
    private UserSpecificGearEntity userLink;
    private String category; // tent / hammock / sleeping bag / sleeping pad / fridge / cooler / solar panel / battery / tool / shower / stove / tarp / cookware / water filter / clothing / consumable / seating / hammock / backpack / Water Proof Dry Bag
    private String brand; // Osprey / Kelty / BlackDiamond / Sawyer / Katadyn / AmazonBrand / Coleman / REI / North Face / Solomon / CamelBack /
    private String model; //Specific name of item, optional
    private String type; // Knife / pot / pan / spoon / Jacket / Pants / Boots / Flashlight / axe / saw / lighter / cordage / chair / table / game / headlamp / fire starter / shovel / first aide / hand sanitation / dish soap / bug spray / sunscreen / games / Water Container
    private String rating; // 50spf / 3season / 20degrees / 20%deet
    private String storage; // 40L / 500watts / 7gal
    private String powerSource; // battery / solar / pump / squeeze / propane / manual
    private String size; // 2person / 6person / Large / small / 10oz
    private double weight;
    private double height;
    private double width;
    private double userScore; // scale of 1-5 stars

    public GearItemsEntity() {
    }

    public GearItemsEntity(String category, String brand, String type) {
        this.category = category;
        this.brand = brand;
        this.type = type;
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

    public double getUserScore() {
        return userScore;
    }

    public void setUserScore(double userScore) {
        this.userScore = userScore;
    }
}
