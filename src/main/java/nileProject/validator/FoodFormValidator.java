package nileProject.validator;

import nileProject.dto.FoodDto;
import nileProject.entity.Food;
import nileProject.form.FoodForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class FoodFormValidator implements Validator {

    @Autowired
    private FoodDto foodDto;

    // This validator only checks for the FoodForm.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == FoodForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        FoodForm foodForm = (FoodForm) target;

        // Check the fields of Food Form.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "NotEmpty.foodForm.code");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.foodForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.foodForm.price");

        String code = foodForm.getCode();
        if (code != null && code.length() > 0) {
            if (code.matches("\\s+")) {
                errors.rejectValue("code", "Pattern.foodForm.code");
            } else if (foodForm.isNewFood()) {
                Food food = foodDto.findFood(code);
                if (food != null) {
                    errors.rejectValue("code", "Duplicate.foodForm.code");
                }
            }
        }
    }


}


