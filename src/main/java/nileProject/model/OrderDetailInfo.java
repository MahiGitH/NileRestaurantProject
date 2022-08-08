package nileProject.model;


import lombok.Data;

//@Data


public class OrderDetailInfo {
    private String id;

    private String foodCode;
    private String foodName;

    private int quanity;
    private double price;
    private double amount;
    public OrderDetailInfo() {

    }
    public OrderDetailInfo(String id, String foodCode, //
                           String foodName, int quanity, double price, double amount) {
        this.id = id;
        this.foodCode = foodCode;
        this.foodName = foodName;
        this.quanity = quanity;
        this.price = price;
        this.amount = amount;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}

