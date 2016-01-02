package de.igorlueckel.magicmirror.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Igor on 19.09.2015.
 */
@Entity
public class Setting {
    @Id
    String settingKey;
    String value;

    public String getKey() {
        return settingKey;
    }

    public void setKey(String key) {
        this.settingKey = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
