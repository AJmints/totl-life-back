package life.totl.totlback.backpack.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//@Entity
public class GearItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String brand; // Osprey / Kelty / BlackDiamond / Sawyer / Katadyn / AmazonBrand / Coleman / REI / North Face / Solomon / CamelBack /
    private String category; // tent / hammock / sleeping bag / sleeping pad / fridge / cooler / solar panel / battery / tool / shower / stove / tarp / cookware / water filter / clothing / consumable / seating / hammock / backpack / Water Proof Dry Bag
    private String type; // Knife / pot / pan / spoon / Jacket / Pants / Boots / Flashlight / axe / saw / lighter / cordage / chair / table / game / headlamp / fire starter / shovel / first aide / hand sanitation / dish soap / bug spray / sunscreen / games
    private String rating; // 50spf / 3season / 20degrees / 20%deet /
    private String storage; // 40L / 500watts /
    private String powerSource; // battery / solar / pump / squeeze / propane / manual
    private String size; // 2person / 6person / Large / small / 10oz
    private double weight;
    private double height;
    private double width;



}
