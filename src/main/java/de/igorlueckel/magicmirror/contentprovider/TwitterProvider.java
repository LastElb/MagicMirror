package de.igorlueckel.magicmirror.contentprovider;

import de.igorlueckel.magicmirror.models.User;
import org.springframework.stereotype.Component;

/**
 * Created by Igor on 21.09.2015.
 */
@Component
public class TwitterProvider extends AbstractContentProvider {
    @Override
    public String getName() {
        return "twitter";
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
