package de.igorlueckel.magicmirror.contentprovider;

import de.igorlueckel.magicmirror.models.User;

import java.security.Principal;

/**
 * Created by Igor on 21.09.2015.
 */
public abstract class AbstractContentProvider {
    public abstract String getName();
    public abstract String getFrontendTemplate();
    public abstract Object getData(User user);
}