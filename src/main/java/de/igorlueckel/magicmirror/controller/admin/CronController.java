package de.igorlueckel.magicmirror.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import de.igorlueckel.magicmirror.contentprovider.ICalProvider;
import de.igorlueckel.magicmirror.contentprovider.ImapProvider;
import de.igorlueckel.magicmirror.models.*;
import de.igorlueckel.magicmirror.repositories.CronActionRepository;
import de.igorlueckel.magicmirror.repositories.UserRepository;
import de.igorlueckel.magicmirror.service.ContentService;
import de.igorlueckel.magicmirror.service.HdmiPowerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Igor on 19.10.2015.
 */
@RestController
@RequestMapping("/admin/cron/")
public class CronController {
    @Autowired
    CronActionRepository cronActionRepository;
    @Autowired
    ContentService contentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskScheduler taskScheduler;

    @Autowired
    ICalProvider iCalProvider;
    @Autowired
    ImapProvider imapProvider;

    @RequestMapping
    @Secured("ROLE_USER")
    ModelAndView index() {
        return new ModelAndView("admin/cron");
    }

    @RequestMapping("actions")
    @ResponseBody
    @Secured("ROLE_USER")
    public List<CronAction> cronActions(Principal principal) {
        return getCronActionByUser(principal.getName());
    }

    List<CronAction> getCronActionByUser(String name) {
        QCronAction qCronAction = QCronAction.cronAction;
        return Lists.newArrayList(cronActionRepository.findAll(qCronAction.user.username.eq(name)));
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public void addCronAction(Principal principal, @RequestBody CronAction cronAction) {
        cronAction.validateCron();
        cronAction.setUser(userRepository.findOneByUsername(principal.getName()).get());
        cronAction = cronActionRepository.save(cronAction);
        addCronToScheduler(cronAction);
    }

    void addCronToScheduler(CronAction cronAction) {
        cronAction.validateCron();
        taskScheduler.schedule(() -> {
            switch (cronAction.getContentProvider()) {
                case "ical":
                    contentService.getUserData(cronAction.getUser(), iCalProvider);
                    break;
                case "imap":
                    contentService.getUserData(cronAction.getUser(), imapProvider);
            }
        }, new CronTrigger(cronAction.getCronSequence()));
    }
}
