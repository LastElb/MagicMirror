package de.igorlueckel.magicmirror.controller;

import de.igorlueckel.magicmirror.service.HdmiPowerManager;
import de.igorlueckel.magicmirror.models.Setting;
import de.igorlueckel.magicmirror.repositories.SettingRepository;
import de.igorlueckel.magicmirror.repositories.UserRepository;
import de.igorlueckel.magicmirror.service.CurrentUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Igor on 19.09.2015.
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    HdmiPowerManager hdmiPowerManager;
    @Autowired
    SettingRepository settingRepository;
    @Autowired
    CurrentUserDetailsService currentUserDetailsService;
    @Autowired
    UserRepository userRepository;

    @RequestMapping("/settings/all")
    Map settings() {
        return settingRepository.findAll().stream().filter(setting -> setting.getValue() != null).collect(Collectors.toMap(Setting::getKey, Setting::getValue));
    }

    @RequestMapping("/speech/input")
    void speechInput(Object input) {

    }
}
