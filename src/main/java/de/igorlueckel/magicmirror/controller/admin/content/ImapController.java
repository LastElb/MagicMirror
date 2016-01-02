package de.igorlueckel.magicmirror.controller.admin.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.igorlueckel.magicmirror.contentprovider.ImapProvider;
import de.igorlueckel.magicmirror.models.QUserSetting;
import de.igorlueckel.magicmirror.models.UserSetting;
import de.igorlueckel.magicmirror.repositories.UserRepository;
import de.igorlueckel.magicmirror.repositories.UserSettingRepository;
import de.igorlueckel.magicmirror.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Igor on 07.10.2015.
 */
@RestController
@RequestMapping("/admin/content/imap/")
public class ImapController {

    @Autowired
    UserSettingRepository userSettingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImapProvider imapProvider;
    @Autowired
    CertificateService certificateService;

    @RequestMapping
    @Secured("ROLE_USER")
    public ModelAndView index(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("admin/content/imap");
        return modelAndView;
    }

    @RequestMapping("accounts")
    @Secured("ROLE_USER")
    public List<ImapProvider.ImapAuthentication> accounts(Principal principal) {
        return accounts(principal.getName(), true);
    }

    public List<ImapProvider.ImapAuthentication> accounts(String name, boolean blankPasswords) {
        QUserSetting qUserSetting = QUserSetting.userSetting;
        Optional<UserSetting> setting = Optional.ofNullable(userSettingRepository.findOne(qUserSetting.user.username.eq(name).and(qUserSetting.settingKey.eq("mail.accounts"))));
        String settingsString = setting.orElse(new UserSetting().setValue("[]")).getValue();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ImapProvider.ImapAuthentication> accounts;
        try {
            accounts = objectMapper.readValue(settingsString, new TypeReference<List<ImapProvider.ImapAuthentication>>() {});
        } catch (IOException e) {
            accounts = new ArrayList<>();
        }
        if (blankPasswords)
            accounts = accounts.stream().map(imapAuthentication -> imapAuthentication.setPassword("")).collect(Collectors.toList());
        return accounts;
    }

    @RequestMapping(value = "set", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public void setAccount(Principal principal, @RequestBody ImapProvider.ImapAuthentication imapAuthentication) {
        QUserSetting qUserSetting = QUserSetting.userSetting;
        Optional<UserSetting> setting = Optional.ofNullable(userSettingRepository.findOne(qUserSetting.user.username.eq(principal.getName()).and(qUserSetting.settingKey.eq("mail.accounts"))));
        String settingsString = setting.orElse(new UserSetting().setValue("[]")).getValue();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ImapProvider.ImapAuthentication> accounts;
        try {
            accounts = objectMapper.readValue(settingsString, new TypeReference<List<ImapProvider.ImapAuthentication>>() {});
        } catch (IOException e) {
            accounts = new ArrayList<>();
        }
        if (accounts.contains(imapAuthentication)) {
            accounts.set(accounts.indexOf(imapAuthentication), imapAuthentication);
        } else
            accounts.add(imapAuthentication);
        UserSetting userSetting = setting.orElse(new UserSetting().setUser(userRepository.findOneByUsername(principal.getName()).get()).setSettingKey("mail.accounts"));
        try {
            userSetting.setValue(objectMapper.writeValueAsString(accounts));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        userSettingRepository.save(userSetting);
    }

    @RequestMapping(value = "test", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public String testAccount(Principal principal, @RequestBody ImapProvider.ImapAuthentication imapAuthentication) throws Exception {
        imapProvider.testConnection(imapAuthentication);
        return "";
    }

    @RequestMapping(value = "trustHost", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public String trustHost(Principal principal, @RequestBody ImapProvider.ImapAuthentication imapAuthentication) throws Exception {
        certificateService.trustCertificate(imapAuthentication.getServer(), 443);
        return "";
    }
}
