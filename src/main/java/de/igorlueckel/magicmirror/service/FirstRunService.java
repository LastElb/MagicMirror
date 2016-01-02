package de.igorlueckel.magicmirror.service;

import de.igorlueckel.magicmirror.enums.Role;
import de.igorlueckel.magicmirror.models.User;
import de.igorlueckel.magicmirror.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Actions that needs to be run after the first run.
 * Created by Igor on 05.10.2015.
 */
@Service
public class FirstRunService {
    @Autowired
    CurrentUserDetailsService currentUserDetailsService;
    @Autowired
    UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Create the initial admin account.
     * The initial state is determined if there are any users saved in the database.
     */
    @PostConstruct
    void postConstruct() {
        if (userRepository.count() == 0) {
            logger.info("No users in database. Creating a new default user.");
            User user = new User().setUsername("admin").setPassword("password").setRole(Role.ADMIN);
            currentUserDetailsService.create(user);
        }
    }
}
