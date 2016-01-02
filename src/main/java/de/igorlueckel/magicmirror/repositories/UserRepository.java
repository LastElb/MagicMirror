package de.igorlueckel.magicmirror.repositories;

import de.igorlueckel.magicmirror.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Optional;

/**
 * Created by Igor on 20.09.2015.
 */
public interface UserRepository extends JpaRepository<User, Integer>, QueryDslPredicateExecutor<User> {
    Optional<User> findOneByUsername(String username);
}
