package de.igorlueckel.magicmirror.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Igor on 18.09.2015.
 */
@Controller
@RequestMapping("/frontend")
public class FrontendController {

    @RequestMapping("/")
    ModelAndView index() {
        return new ModelAndView("frontend/index");
    }

    @RequestMapping("")
    String redirect() {
        return "redirect:./";
    }
}
