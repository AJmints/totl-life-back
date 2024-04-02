package life.totl.totlback.backpack.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GearItemDTO {

    private long userId;
    private String category; // required
    private String brand;
    private String type;
    private String size;
    private String model;
    private String rating;
    private String storage;
    private String powerSource;
    private String extraInfo;
    private double weight;
    private double height;
    private double width;
    private double length;
    private String additionalDetails;
    private boolean lendable;
    private int quantity;
    private String itemCondition;
    private boolean forSale;
    private double price;
    private boolean hidden;
}
