package de.igorlueckel.magicmirror.models;

import org.springframework.scheduling.support.CronTrigger;

import javax.persistence.*;

/**
 * Created by Igor on 05.10.2015.
 */
@Entity
public class CronAction {

    @Id
    @GeneratedValue
    Integer id;
    String cronSequence;
    @OneToOne
    User user;
    String contentProvider;

    public void validateCron() {
        CronTrigger cronTrigger = new CronTrigger(cronSequence);
    }

    public Integer getId() {
        return id;
    }

    public CronAction setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCronSequence() {
        return cronSequence;
    }

    public CronAction setCronSequence(String cronSequence) {
        this.cronSequence = cronSequence;
        return this;
    }

    public User getUser() {
        return user;
    }

    public CronAction setUser(User user) {
        this.user = user;
        return this;
    }

    public String getContentProvider() {
        return contentProvider;
    }

    public CronAction setContentProvider(String contentProvider) {
        this.contentProvider = contentProvider;
        return this;
    }
}
