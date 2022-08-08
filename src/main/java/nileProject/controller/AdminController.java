package nileProject.controller;



import nileProject.dto.FoodDto;
import nileProject.dto.OrderDto;
import nileProject.entity.Food;
import nileProject.form.FoodForm;
import nileProject.model.OrderDetailInfo;
import nileProject.model.OrderInfo;
import nileProject.pagination.PaginationResult;
import nileProject.validator.FoodFormValidator;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Transactional
//@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private FoodDto foodDto;

    @Autowired
    private FoodFormValidator foodFormValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == FoodForm.class) {
            dataBinder.setValidator(foodFormValidator);
        }
    }

    @RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
    public String login(Model model) {

        return "login";
    }

    @RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.GET)
    public String accountInfo(Model model) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getPassword());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.isEnabled());

        model.addAttribute("userDetails", userDetails);
        return "accountInfo";
    }

    @RequestMapping(value = { "/admin/orderList" }, method = RequestMethod.GET)
    public String orderList(Model model, //
                            @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }
        final int MAX_RESULT = 5;
        final int MAX_NAVIGATION_PAGE = 10;

        PaginationResult<OrderInfo> paginationResult //
                = orderDto.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);

        model.addAttribute("paginationResult", paginationResult);
        return "orderList";
    }
    // GET: Show food
    @RequestMapping(value = { "/admin/food" }, method = RequestMethod.GET)
    public String food(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        FoodForm foodForm = null;

        if (code != null && code.length() > 0) {
            Food food = foodDto.findFood(code);
            if (food != null) {
                foodForm = new FoodForm(food);
            }
        }
        if (foodForm == null) {
            foodForm = new FoodForm();
            foodForm.setNewFood(true);
        }
        model.addAttribute("foodForm", foodForm);
        return "food";
    }
    // POST: Save food
    @RequestMapping(value = { "/admin/food" }, method = RequestMethod.POST)
    public String foodSave(Model model, //
                           @ModelAttribute("foodForm") @Validated FoodForm foodForm, //
                           BindingResult result, //
                           final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "food";
        }
        try {
            foodDto.save(foodForm);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("errorMessage", message);
            // Show food form.
            return "food";
        }

        return "redirect:/foodList";
    }

    @RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderDto.getOrderInfo(orderId);
        }
        if (orderInfo == null) {
            return "redirect:/admin/orderList";
        }
        List<OrderDetailInfo> details = this.orderDto.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);

        model.addAttribute("orderInfo", orderInfo);

        return "order";
    }

}
