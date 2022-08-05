package nileProject.model;


import lombok.Data;
import nileProject.form.CustomerForm;

@Data

public class CustomerInfo {

    private String name;
    private String address;
    private String email;
    private String phone;

    private boolean valid;

    public CustomerInfo() {

    }
    public CustomerInfo(CustomerForm customerForm) {
        this.name = customerForm.getName();
        this.address = customerForm.getAddress();
        this.email = customerForm.getEmail();
        this.phone = customerForm.getPhone();
        this.valid = customerForm.isValid();
    }
}

