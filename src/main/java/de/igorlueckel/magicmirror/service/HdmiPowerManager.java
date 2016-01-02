package de.igorlueckel.magicmirror.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Igor on 19.09.2015.
 */
@Service
public class HdmiPowerManager {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public void powerOff() {
        try {
            Runtime.getRuntime().exec("sudo /usr/sbin/raspi-monitor off", new String[]{"/usr/sbin"});
            logger.info("Disabling HDMI output");
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void powerOn() {
        try {
            Runtime.getRuntime().exec("sudo /usr/sbin/raspi-monitor on", new String[]{"/usr/sbin"});
            logger.info("Enabling HDMI output");
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
