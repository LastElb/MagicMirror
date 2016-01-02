package de.igorlueckel.magicmirror.service;

import de.igorlueckel.magicmirror.contentprovider.AbstractContentProvider;
import de.igorlueckel.magicmirror.contentprovider.ICalProvider;
import de.igorlueckel.magicmirror.contentprovider.ImapProvider;
import de.igorlueckel.magicmirror.models.User;
import de.igorlueckel.magicmirror.repositories.CronActionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.NotSupportedException;
import java.util.*;

/**
 * Created by Igor on 05.10.2015.
 */
@Service
public class ContentService {

    List<AbstractContentProvider> contentProviders;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    ICalProvider iCalProvider;
    @Autowired
    ImapProvider imapProvider;
    @Autowired
    CronActionRepository cronActionRepository;
    @Autowired
    TaskScheduler taskScheduler;

    @PostConstruct
    void postConstruct() {
        contentProviders = new ArrayList<>();
        contentProviders.add(iCalProvider);
        contentProviders.add(imapProvider);

        cronActionRepository.findAll().stream().forEach(cronAction -> {
            CronTrigger cronTrigger = new CronTrigger(cronAction.getCronSequence());
            for (AbstractContentProvider abstractContentProvider : contentProviders)
                if (abstractContentProvider.getName().equals(cronAction.getContentProvider()))
                    taskScheduler.schedule(() -> {getUserData(cronAction.getUser(),abstractContentProvider);}, cronTrigger);
        });
    }

    public void sendAllData() {
        User user = new User().setUsername("Igor");
        getUserData(user);
    }

    public void getUserData(User user, AbstractContentProvider abstractContentProvider) {
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("user", user);

        List<Object> content = new ArrayList<>();
        Map<String, Object> providerData = new LinkedHashMap<>();
        providerData.put("data", abstractContentProvider.getData(user));
        providerData.put("templateUrl", abstractContentProvider.getFrontendTemplate());
        content.add(providerData);

        dataMap.put("content", content);
        template.convertAndSend("/websocket", dataMap);
    }

    public void getUserData(User user) {
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("user", user);

        List<Object> content = new ArrayList<>();
        contentProviders.stream().forEach(abstractContentProvider -> {
            Map<String, Object> providerData = new LinkedHashMap<>();
            providerData.put("data", abstractContentProvider.getData(user));
            providerData.put("templateUrl", abstractContentProvider.getFrontendTemplate());
            content.add(providerData);
        });

        dataMap.put("content", content);
        template.convertAndSend("/websocket", dataMap);
    }
}
