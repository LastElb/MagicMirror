package de.igorlueckel.magicmirror.controller.admin;

import de.igorlueckel.magicmirror.enums.Role;
import de.igorlueckel.magicmirror.models.User;
import de.igorlueckel.magicmirror.repositories.UserRepository;
import de.igorlueckel.magicmirror.service.CurrentUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Igor on 01.10.2015.
 */
@Controller
@RequestMapping("/admin/users/")
public class UsersController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrentUserDetailsService currentUserDetailsService;

    @RequestMapping(method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public ModelAndView users() {
        ModelAndView modelAndView = new ModelAndView("admin/users");
        modelAndView.addObject("users", userRepository.findAll());
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    public String addUser(@ModelAttribute User user) {
        user.setRole(Role.USER);
        currentUserDetailsService.create(user);
        return "redirect:./";
    }
}
