package de.igorlueckel.magicmirror.contentprovider;

import de.igorlueckel.magicmirror.models.User;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Created by Igor on 21.09.2015.
 */
@Component
public class FacebookProvider extends AbstractContentProvider {
    @Override
    public String getName() {
        return "facebook";
    }

    @Override
    public String getFrontendTemplate() {
        return null;
    }

    @Override
    public Object getData(User user) {

        return null;
    }
}
