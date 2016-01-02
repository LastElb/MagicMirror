package de.igorlueckel.magicmirror.contentprovider;

import de.igorlueckel.magicmirror.controller.admin.content.ImapController;
import de.igorlueckel.magicmirror.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import java.security.Security;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Igor on 07.10.2015.
 */
@Component
public class ImapProvider extends AbstractContentProvider {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ImapController imapController;

    @Override
    public String getName() {
        return "imap";
    }

    @Override
    public String getFrontendTemplate() {
        return "imap.html";
    }

    @Override
    public Object getData(User user) {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        Map<String, Integer> unreadMessages = new LinkedHashMap<>();

        try {
            Session session = Session.getInstance(props);
            Store store = session.getStore();

            for (ImapAuthentication imapAuthentication : imapController.accounts(user.getUsername(), false)) {
                try {
                    store.connect(imapAuthentication.getServer(), imapAuthentication.getUser(), imapAuthentication.getPassword());
                    Folder inbox = store.getFolder("INBOX");
                    inbox.open(Folder.READ_ONLY);
                    unreadMessages.put(imapAuthentication.getEmail(), inbox.getUnreadMessageCount());
                    store.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception mex) {
            mex.printStackTrace();
        }
        return unreadMessages;
    }

    public void testConnection(ImapAuthentication imapAuthentication) throws Exception{
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props);
        Store store = session.getStore();
        store.connect(imapAuthentication.getServer(), imapAuthentication.getUser(), imapAuthentication.getPassword());
        store.close();
    }

    public static class ImapAuthentication {
        String server;
        String user;
        String password;
        String email;

        public String getServer() {
            return server;
        }

        public ImapAuthentication setServer(String server) {
            this.server = server;
            return this;
        }

        public String getUser() {
            return user;
        }

        public ImapAuthentication setUser(String user) {
            this.user = user;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public ImapAuthentication setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public ImapAuthentication setEmail(String email) {
            this.email = email;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ImapAuthentication that = (ImapAuthentication) o;

            if (!server.equals(that.server)) return false;
            if (!user.equals(that.user)) return false;
            return email.equals(that.email);

        }

        @Override
        public int hashCode() {
            int result = server.hashCode();
            result = 31 * result + user.hashCode();
            result = 31 * result + email.hashCode();
            return result;
        }
    }
}
