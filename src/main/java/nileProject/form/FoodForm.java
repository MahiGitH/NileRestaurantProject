package nileProject.form;

import lombok.Data;
import nileProject.entity.Food;
import org.springframework.web.multipart.MultipartFile;

@Data

public class FoodForm {
    private String code;
    private String name;
    private double price;

    private boolean newFood = false;

    // Upload file.
    private MultipartFile fileData;

    public FoodForm() {
        this.newFood= true;
    }
    public FoodForm(Food food) {
        this.code = food.getCode();
        this.name = food.getName();
        this.price = food.getPrice();
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

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }

    public boolean isNewFood() {
        return newFood;
    }

    public void setNewFood(boolean newFood) {
        this.newFood = newFood;
    }
}
