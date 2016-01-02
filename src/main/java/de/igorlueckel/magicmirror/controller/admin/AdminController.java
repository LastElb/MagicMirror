package de.igorlueckel.magicmirror.controller.admin;

import de.igorlueckel.magicmirror.models.Setting;
import de.igorlueckel.magicmirror.repositories.SettingRepository;
import de.igorlueckel.magicmirror.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Igor on 19.09.2015.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    SettingRepository settingRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping("")
    String redirect() {
        return "redirect:./";
    }

    @RequestMapping("/")
    ModelAndView dashboard() {
        return new ModelAndView("admin/dashboard");
    }

    @RequestMapping("/weather/")
    ModelAndView weather() {
        return new ModelAndView("admin/weather");
    }

    @RequestMapping(value = "/settings/set", method = RequestMethod.POST)
    void setSettings(@RequestBody List<Setting> settings) {
        settingRepository.save(settings);
    }
}
