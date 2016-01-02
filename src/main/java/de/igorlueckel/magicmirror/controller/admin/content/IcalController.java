package de.igorlueckel.magicmirror.controller.admin.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.igorlueckel.magicmirror.contentprovider.ICalProvider;
import de.igorlueckel.magicmirror.contentprovider.ImapProvider;
import de.igorlueckel.magicmirror.models.QUserSetting;
import de.igorlueckel.magicmirror.models.UserSetting;
import de.igorlueckel.magicmirror.repositories.UserRepository;
import de.igorlueckel.magicmirror.repositories.UserSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/admin/content/ical/")
public class IcalController {

    @Autowired
    UserSettingRepository userSettingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ICalProvider iCalProvider;

    @RequestMapping
    @Secured("ROLE_USER")
    public ModelAndView index(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("admin/content/ical");
        return modelAndView;
    }

    @RequestMapping("accounts")
    @Secured("ROLE_USER")
    public List<ICalProvider.CalendarConnection> accounts(Principal principal) {
        return accounts(principal.getName());
    }

    public List<ICalProvider.CalendarConnection> accounts(String name) {
        QUserSetting qUserSetting = QUserSetting.userSetting;
        Optional<UserSetting> setting = Optional.ofNullable(userSettingRepository.findOne(qUserSetting.user.username.eq(name).and(qUserSetting.settingKey.eq("calendar.accounts"))));
        String settingsString = setting.orElse(new UserSetting().setValue("[]")).getValue();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ICalProvider.CalendarConnection> accounts;
        try {
            accounts = objectMapper.readValue(settingsString, new TypeReference<List<ICalProvider.CalendarConnection>>() {});
        } catch (IOException e) {
            accounts = new ArrayList<>();
        }
        return accounts;
    }

    @RequestMapping(value = "set", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public void setAccount(Principal principal, @RequestBody ICalProvider.CalendarConnection calendarConnection) {
        QUserSetting qUserSetting = QUserSetting.userSetting;
        Optional<UserSetting> setting = Optional.ofNullable(userSettingRepository.findOne(qUserSetting.user.username.eq(principal.getName()).and(qUserSetting.settingKey.eq("calendar.accounts"))));
        String settingsString = setting.orElse(new UserSetting().setValue("[]")).getValue();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ICalProvider.CalendarConnection> accounts;
        try {
            accounts = objectMapper.readValue(settingsString, new TypeReference<List<ICalProvider.CalendarConnection>>() {});
        } catch (IOException e) {
            accounts = new ArrayList<>();
        }
        if (accounts.contains(calendarConnection)) {
            accounts.set(accounts.indexOf(calendarConnection), calendarConnection);
        } else
            accounts.add(calendarConnection);
        UserSetting userSetting = setting.orElse(new UserSetting().setUser(userRepository.findOneByUsername(principal.getName()).get()).setSettingKey("calendar.accounts"));
        try {
            userSetting.setValue(objectMapper.writeValueAsString(accounts));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        userSettingRepository.save(userSetting);
    }

    @RequestMapping(value = "test", method = RequestMethod.POST, produces = "application/json")
    @Secured("ROLE_USER")
    @ResponseBody
    public Object testAccount(Principal principal, @RequestBody ICalProvider.CalendarConnection calendarConnection) throws Exception {
        if (iCalProvider.testConnection(calendarConnection) == null)
            throw new Exception("Could not parse calendar");
        return null;
    }
}
