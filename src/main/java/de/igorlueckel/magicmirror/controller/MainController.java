package de.igorlueckel.magicmirror.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Igor on 19.09.2015.
 */
@Controller
public class MainController { //implements ErrorController {

    @RequestMapping("/")
    String redirect() {
        return "redirect:frontend/";
    }

    @RequestMapping("")
    String redirect2() {
        return "redirect:frontend/";
    }

    @RequestMapping("/login")
    ModelAndView login() {
        return new ModelAndView("login");
    }

//    @RequestMapping("/error")
//    public String error(HttpServletRequest request, Model model) {
//        model.addAttribute("errorCode", request.getAttribute("javax.servlet.error.status_code"));
//        model.addAttribute("errorMessage", request.getAttribute("javax.servlet.error.message"));
//        return "error";
//    }

    //@Override
    public String getErrorPath() {
        return "/error";
    }
}
