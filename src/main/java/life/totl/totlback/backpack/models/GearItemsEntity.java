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

    public GearItemsEntity(String category, String type) {
        this.category = category;
        this.type = type;
    }

    public GearItemsEntity() {
    }
}
