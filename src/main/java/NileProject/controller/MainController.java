package NileProject.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String index(){

        return "index";
    }

    @GetMapping("/order")
    public String getOrderPage() {

        return "order/index.html";
    }

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
}
