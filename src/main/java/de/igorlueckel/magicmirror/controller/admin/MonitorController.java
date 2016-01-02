package de.igorlueckel.magicmirror.controller.admin;

import de.igorlueckel.magicmirror.service.HdmiPowerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Igor on 19.10.2015.
 */
@Controller
@RequestMapping("/admin/monitor/")
public class MonitorController {
    @Autowired
    HdmiPowerManager hdmiPowerManager;

    @RequestMapping()
    ModelAndView index() {
        return new ModelAndView("admin/monitor");
    }

    @RequestMapping("off")
    @ResponseBody
    void powerOff() {
        hdmiPowerManager.powerOff();
    }

    @RequestMapping("on")
    @ResponseBody
    void powerOn() {
        hdmiPowerManager.powerOn();
    }
}
