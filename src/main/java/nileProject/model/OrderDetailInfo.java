package nileProject.model;


import lombok.Data;

@Data


public class OrderDetailInfo {
    private String id;

    private String foodCode;
    private String foodName;

    private int quanity;
    private double price;
    private double amount;

    // Using for JPA/Hibernate Query.
    public OrderDetailInfo(String id, String foodCode, //
                           String foodName, int quanity, double price, double amount) {
        this.id = id;
        this.foodCode = foodCode;
        this.foodName = foodName;
        this.quanity = quanity;
        this.price = price;
        this.amount = amount;
    }
}

