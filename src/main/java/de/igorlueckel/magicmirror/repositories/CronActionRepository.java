package de.igorlueckel.magicmirror.repositories;

import de.igorlueckel.magicmirror.models.CronAction;
import de.igorlueckel.magicmirror.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by Igor on 19.09.2015.
 */
public interface CronActionRepository extends JpaRepository<CronAction, Integer>, QueryDslPredicateExecutor<CronAction> {
}
