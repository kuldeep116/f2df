package com.springboot.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.service.ProductCategoryService;

@Controller
public class WelcomeController {
	
    @Autowired
    ProductCategoryService categoryService;
    
    @RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView mereDukanView(@RequestBody RequestProduct req) {
		ModelAndView mav = new ModelAndView();
		System.out.println("we are in welcome method ");
		mav.setViewName("/new-f2df/index.html");
		return mav;
	}
    @RequestMapping(value = "/new-f2df/contact", method = RequestMethod.GET)
   	public ModelAndView contactUs(@RequestBody RequestProduct req) {
   		ModelAndView mav = new ModelAndView();
   		System.out.println("we are in welcome method ");
   		mav.setViewName("/contact.html");
   		return mav;
   	}

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String showWelcomePage(ModelMap model) {
    	System.out.println("we are in welcome method ");
        model.put("catList", categoryService.getCategoryList(0,10,""));
        return "index";
    }
    @RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String feature(Model model) {
		System.out.println("we are in welcome method ");
		//model.addAttribute("categoryList", categoryService.getCategoryList(0, 34, ""));
		// model.put("catList", categoryService.getCategoryList(0,10,""));
		return "/new-f2df/contact.html";
	}
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(ModelAndView model) {
    	System.out.println("we are in welcome method ");
    	int randomPIN = (int) (Math.random() * 90000) + 10000;
    	// insert into table with phone no
    	//if (!userInfos.getCellPhone().equals("")) {
			//util.sendSecretCodeMessage(String.valueOf(randomPIN), userInfos.getCellPhone());
			//response.setMessage("Success");
		//}
       // model.put("catList", categoryService.getCategoryList(0,10));
        return "login.html";
    }

    private String getLoggedinUserName() {
        Object principal = SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        return principal.toString();
    }

}