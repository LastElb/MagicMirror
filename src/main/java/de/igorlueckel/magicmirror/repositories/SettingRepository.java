package de.igorlueckel.magicmirror.repositories;

import de.igorlueckel.magicmirror.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by Igor on 19.09.2015.
 */
public interface SettingRepository extends JpaRepository<Setting, Integer>, QueryDslPredicateExecutor<Setting> {
}
