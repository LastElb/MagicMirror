package de.igorlueckel.magicmirror.service;

import de.igorlueckel.magicmirror.enums.Role;
import de.igorlueckel.magicmirror.models.User;
import de.igorlueckel.magicmirror.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Igor on 20.09.2015.
 */
@Component
public class CurrentUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(s).orElseThrow(() -> new UsernameNotFoundException("Username " + s + " not found"));
        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        if (user.getRole().equals(Role.ADMIN))
            auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
        return new org.springframework.security.core.userdetails.User(s, user.getPassword(),
                auth);
    }

    public User create(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }
}
