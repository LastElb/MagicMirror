package de.igorlueckel.magicmirror.controller;

import de.igorlueckel.magicmirror.contentprovider.ICalProvider;
import de.igorlueckel.magicmirror.contentprovider.ImapProvider;
import de.igorlueckel.magicmirror.controller.admin.content.ImapController;
import de.igorlueckel.magicmirror.models.User;
import de.igorlueckel.magicmirror.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by Igor on 16.10.2015.
 */
@RequestMapping("/test/")
@RestController
public class TestController {

    @Autowired
    ImapProvider imapProvider;
    @Autowired
    ICalProvider iCalProvider;
    @Autowired
    ContentService contentService;

    @RequestMapping("mail")
    Object testMail() {
        return imapProvider.getData(new User().setUsername("Igor"));
    }

    @RequestMapping("calendar")
    Object testCalendar() {
        return iCalProvider.getData(new User().setUsername("Igor"));
    }

    @RequestMapping("push")
    void testPush() {
        contentService.sendAllData();
    }
}
