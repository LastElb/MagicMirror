package de.igorlueckel.magicmirror.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
public class StartupHousekeeper implements ApplicationListener<ContextRefreshedEvent> {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Does not work
     * @param event
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        // do whatever you need here

    }
}