package de.igorlueckel.magicmirror.models;

import javax.persistence.*;

/**
 * Created by Igor on 02.10.2015.
 */
@Entity
public class UserSetting {
    @Id
    @GeneratedValue
    Integer id;

    @OneToOne
    User user;
    String settingKey;

    @Column(length = 25500)
    String value;

    public User getUser() {
        return user;
    }

    public UserSetting setUser(User user) {
        this.user = user;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public UserSetting setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public UserSetting setSettingKey(String settingKey) {
        this.settingKey = settingKey;
        return this;
    }

    public String getValue() {
        return value;
    }

    public UserSetting setValue(String value) {
        this.value = value;
        return this;
    }
}
