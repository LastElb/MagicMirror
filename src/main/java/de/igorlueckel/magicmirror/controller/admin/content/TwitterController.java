package de.igorlueckel.magicmirror.controller.admin.content;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Igor on 02.10.2015.
 */
@Controller
@RequestMapping("/admin/content/twitter/")
public class TwitterController {

    @RequestMapping
    @Secured("ROLE_USER")
    public ModelAndView index() {
        return new ModelAndView("admin/content/twitter");
    }

//    @RequestMapping("connect")
//    public void connect(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Facebook facebook = new FacebookFactory().getInstance();
//        request.getSession().setAttribute("facebook", facebook);
//        StringBuffer callbackURL = request.getRequestURL();
//        int index = callbackURL.lastIndexOf("/");
//        callbackURL.replace(index, callbackURL.length(), "").append("/callback");
//        response.sendRedirect(facebook.getOAuthAuthorizationURL(callbackURL.toString()));
//    }
//
//    @RequestMapping("callback")
//    public void callback(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FacebookException {
//        Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
//        String oauthCode = request.getParameter("code");
//        try {
//            facebook.getOAuthAccessToken(oauthCode);
//        } catch (FacebookException e) {
//            throw new ServletException(e);
//        }
//        //facebook.notifications().getNotifications().stream().forEach(notification -> System.out.println("notification = " + notification.getTitle()));
//        //facebook.getHome().stream().forEach(post -> System.out.println("post = " + post.getDescription()));
//        //response.sendRedirect(request.getContextPath() + "/");
//    }
}
