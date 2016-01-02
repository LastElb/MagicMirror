package de.igorlueckel.magicmirror.repositories;

import de.igorlueckel.magicmirror.models.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by Igor on 05.10.2015.
 */
public interface UserSettingRepository extends JpaRepository<UserSetting, Integer>, QueryDslPredicateExecutor<UserSetting> {
}
