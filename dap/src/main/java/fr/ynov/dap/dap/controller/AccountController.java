package fr.ynov.dap.dap.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.ynov.dap.dap.google.service.GoogleAccount;
import fr.ynov.dap.dap.helper.AuthHelper;

/**
 * used to connect or retrieve token for an userID.
 *
 * @author David_tepoche
 *
 */
@RestController
public class AccountController extends BaseController {

    /**
     * link googleAccount service.
     */
    @Autowired
    private GoogleAccount googleAccount;

    /**
     * Add a Google account (user will be prompt to connect and accept required
     * access).
     *
     * @param userKey     user in bdd
     * @param accountName alis for googleAccount
     * @param request     the HTTP request
     * @param session     the HTTP session
     * @return the view to Display (on Error)
     * @throws GeneralSecurityException throw if the addCount fail
     * @throws IOException              throw if the call from accountService fail
     */
    @GetMapping("/account/add/{userKey}/{accountName}")
    public String addAccount(@PathVariable("userKey") final String userKey,
            @PathVariable("accountName") final String accountName, final HttpServletRequest request,
            final HttpSession session) throws GeneralSecurityException, IOException {
        return googleAccount.addAccount(accountName, userKey, request, session);
    }

    /**
     * retrieve the token send by google and store it.
     *
     * @param code    code return from google service
     * @param request the resquet given
     * @param session session open
     * @return redirection to google.
     * @throws ServletException throw if the call from googleAccount fail
     */
    @RequestMapping("/oAuth2Callback")
    public String oAuthCallback(@RequestParam final String code, final HttpServletRequest request,
            final HttpSession session) throws ServletException {
        return googleAccount.oAuthCallback(code, request, session);
    }

    @Override
    public final String getClassName() {
        return AccountController.class.getName();
    }

    /**
     * get the index page, in order to login .
     * 
     * @param model   an object look like httpSession ?
     * @param request the httpRequest from client
     * @return index
     */
    @GetMapping("/index")
    public String index(final Model model, final HttpServletRequest request) {
        UUID state = UUID.randomUUID();
        UUID nonce = UUID.randomUUID();

        // Save the state and nonce in the session so we can
        // verify after the auth process redirects back
        HttpSession session = request.getSession();
        session.setAttribute("expected_state", state);
        session.setAttribute("expected_nonce", nonce);

        String loginUrl = AuthHelper.getLoginUrl(state, nonce);
        model.addAttribute("loginUrl", loginUrl);
        // Name of a definition in WEB-INF/defs/pages.xml
        return "index";
    }

}
