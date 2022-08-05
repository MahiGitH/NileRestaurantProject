package nileProject.model;

import lombok.Data;
import nileProject.entity.Food;

@Data

public class FoodInfo {
    private String code;
    private String name;
    private double price;

    public FoodInfo(Food food) {
        this.code = food.getCode();
        this.name = food.getName();
        this.price = food.getPrice();
    }


    // Using in JPA/Hibernate query
    public FoodInfo(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}

