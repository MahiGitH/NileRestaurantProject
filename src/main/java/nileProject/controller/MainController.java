package nileProject.controller;


import nileProject.dto.FoodDto;
import nileProject.dto.OrderDto;
import nileProject.entity.Food;
import nileProject.form.CustomerForm;
import nileProject.model.CartInfo;
import nileProject.model.CustomerInfo;
import nileProject.model.FoodInfo;
import nileProject.pagination.PaginationResult;
import nileProject.utils.Utils;
import nileProject.validator.CustomerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class MainController {

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private FoodDto foodDto;

    @Autowired
    private CustomerFormValidator customerFormValidator;


    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        // Case update quantity in cart
        // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
        if (target.getClass() == CartInfo.class) {

        }

        // Case save customer information.
        // (@ModelAttribute @Validated CustomerInfo customerForm)
        else if (target.getClass() == CustomerForm.class) {
            dataBinder.setValidator(customerFormValidator);
        }

    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    // Food List

    // Product List
    @RequestMapping({ "/admin/order" })
    public String listProductHandler(Model model, //
                                     @RequestParam(value = "name", defaultValue = "") String likeName,
                                     @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 10;
        final int maxNavigationPage = 10;

        PaginationResult<FoodInfo> result = foodDto.queryFoods(page, //
                maxResult, maxNavigationPage, likeName);

        model.addAttribute("paginationFoods", result);
        return "admin/order/index.html";
    }

    @RequestMapping({ "/order" })
    public String listFoodHandler(Model model, //
                                     @RequestParam(value = "name", defaultValue = "") String likeName,
                                     @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 10;
        final int maxNavigationPage = 10;

        PaginationResult<FoodInfo> result = foodDto.queryFoods(page, //
                maxResult, maxNavigationPage, likeName);

        model.addAttribute("paginationfoods", result);
        return "order/index.html";
    }

    @RequestMapping({ "/buyFood" })
    public String listFoodHandler(HttpServletRequest request, Model model, //
                                     @RequestParam(value = "code", defaultValue = "") String code) {

        Food food = null;
        if (code != null && code.length() > 0) {
            food = foodDto.findFood(code);
        }
        if (food != null) {

            //
            CartInfo cartInfo = Utils.getCartInSession(request);

            FoodInfo foodInfo = new FoodInfo(food);

            cartInfo.addFood(foodInfo, 1);
        }

        return "redirect:/shoppingCart";
    }

    @RequestMapping({ "/shoppingCartRemoveFood" })
    public String removeFoodHandler(HttpServletRequest request, Model model, //
                                    @RequestParam(value = "code", defaultValue = "") String code) {
        Food food = null;
        if (code != null && code.length() > 0) {
            food = foodDto.findFood(code);
        }
        if (food != null) {

            CartInfo cartInfo = Utils.getCartInSession(request);

            FoodInfo foodInfo = new FoodInfo(food);

            cartInfo.removeFood(foodInfo);

        }

        return "redirect:/shoppingCart";
    }

    // POST: Update quantity for food in cart
    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
    public String shoppingCartUpdateQty(HttpServletRequest request, //
                                        Model model, //
                                        @ModelAttribute("cartForm") CartInfo cartForm) {

        CartInfo cartInfo = Utils.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);

        return "redirect:/shoppingCart";
    }

    // GET: Show cart.
    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = Utils.getCartInSession(request);

        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }

    // GET: Enter customer information.
    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {

        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        }
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();

        CustomerForm customerForm = new CustomerForm(customerInfo);

        model.addAttribute("customerForm", customerForm);

        return "shoppingCartCustomer";
    }

    // POST: Save customer information.
    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(HttpServletRequest request, //
                                           Model model, //
                                           @ModelAttribute("customerForm") @Validated CustomerForm customerForm, //
                                           BindingResult result, //
                                           final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            customerForm.setValid(false);
            // Forward to reenter customer info.
            return "shoppingCartCustomer";
        }

        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);
        CustomerInfo customerInfo = new CustomerInfo(customerForm);
        cartInfo.setCustomerInfo(customerInfo);

        return "redirect:/shoppingCartConfirmation";
    }

    // GET: Show information to confirm.
    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo == null || cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {

            return "redirect:/shoppingCartCustomer";
        }
        model.addAttribute("myCart", cartInfo);

        return "shoppingCartConfirmation";
    }

    // POST: Submit Cart (Save)
    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)

    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {

            return "redirect:/shoppingCartCustomer";
        }
        try {
            orderDto.saveOrder(cartInfo);
        } catch (Exception e) {

            return "shoppingCartConfirmation";
        }

        // Remove Cart from Session.
        Utils.removeCartInSession(request);

        // Store last cart.
        Utils.storeLastOrderedCartInSession(request, cartInfo);

        return "redirect:/shoppingCartFinalize";
    }

    @RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
    public String shoppingCartFinalize(HttpServletRequest request, Model model) {

        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);

        if (lastOrderedCart == null) {
            return "redirect:/shoppingCart";
        }
        model.addAttribute("lastOrderedCart", lastOrderedCart);
        return "shoppingCartFinalize";
    }

    @RequestMapping(value = { "/foodImage" }, method = RequestMethod.GET)
    public void foodImage(HttpServletRequest request, HttpServletResponse response, Model model,
                          @RequestParam("code") String code) throws IOException {
        Food food = null;
        if (code != null) {
            food = this.foodDto.findFood(code);
        }
        if (food != null && food.getImage() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(food.getImage());
        }
        response.getOutputStream().close();
    }


    @GetMapping("/")
    public String index(){

        return "index";
    }

//    @GetMapping("/order")
//    public String getOrderPage() {
//
//        return "order/login.html";
//    }

    @GetMapping("/contact")
    public String getContactPage() {
        return "contact/contact1.html";
    }

    @GetMapping("/about")
    public String getAboutPage() {

        return "about/index.html";
    }

    @GetMapping("/community")
    public String getCommunityPage() {

        return "community/index.html";
    }

    @GetMapping("/press")
    public String getPressPage() {

        return "press/press.html";
    }
    @GetMapping("/menu")
        public String getMenuPage() {

            return "menu/index.html";
        }
}
