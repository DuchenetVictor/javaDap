package fr.ynov.dap.dap.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.oauth2.TokenResponse;

import fr.ynov.dap.dap.SecretFileAccesException;
import fr.ynov.dap.dap.google.service.GoogleAccountService;
import fr.ynov.dap.dap.microsoft.services.MicrosoftAccountService;
import fr.ynov.dap.dap.model.IdToken;

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
    private GoogleAccountService googleAccountService;
    /**
     * link microsoftAccount service.
     */
    @Autowired
    private MicrosoftAccountService microsoftAccountService;

    /**
     * Add a Google account (user will be prompt to connect and accept required
     * access).
     *
     * @param userKey     user in bdd
     * @param accountName alis for googleAccount
     * @param request     the HTTP request
     * @param session     the HTTP session
     * @param response    dunno
     * @throws GeneralSecurityException throw if the addCount fail
     * @throws IOException              throw if the call from accountService fail
     */
    @GetMapping("/account/add/{userKey}/{accountName}")
    public void addAccount(@PathVariable("userKey") final String userKey,
            @PathVariable("accountName") final String accountName, final HttpServletRequest request,
            final HttpSession session, final HttpServletResponse response)
            throws GeneralSecurityException, IOException {
        googleAccountService.addAccount(accountName, userKey, request, session, response);
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
        return googleAccountService.oAuthCallback(code, request, session);
    }

    @Override
    public final String getClassName() {
        return AccountController.class.getName();
    }

    /**
     * get the index page, in order to login .
     *
     * @param model    an object look like httpSession
     * @param request  the httpRequest from client
     * @param response dunno.
     * @throws IOException              dunno.
     * @throws SecretFileAccesException dunno
     */
    @GetMapping("/index")
    public void index(final HttpServletResponse response, final Model model, final HttpServletRequest request)
            throws IOException, SecretFileAccesException {
        UUID state = UUID.randomUUID();
        UUID nonce = UUID.randomUUID();

        // Save the state and nonce in the session so we can
        // verify after the auth process redirects back
        HttpSession session = request.getSession();
        session.setAttribute("expected_state", state);
        session.setAttribute("expected_nonce", nonce);

        String loginUrl = null;
        loginUrl = microsoftAccountService.getLoginUrl(state, nonce);

        response.sendRedirect(loginUrl);
    }

    /**
     * dunno.
     *
     * @param code    dunno
     * @param idToken dunno
     * @param state   dunno
     * @param request dunno
     * @return dunno
     * @throws SecretFileAccesException dunno
     */
    @PostMapping(value = "/authorize")
    public String authorize(@RequestParam("code") final String code, @RequestParam("id_token") final String idToken,
            @RequestParam("state") final UUID state, final HttpServletRequest request) throws SecretFileAccesException {

        // Get the expected state value from the session
        HttpSession session = request.getSession();
        UUID expectedState = (UUID) session.getAttribute("expected_state");
        UUID expectedNonce = (UUID) session.getAttribute("expected_nonce");

        // Make sure that the state query parameter returned matches
        // the expected state
        if (state.equals(expectedState)) {
            IdToken idTokenObj = IdToken.parseEncodedToken(idToken, expectedNonce.toString());
            if (idTokenObj != null) {
                TokenResponse tokenResponse;
                tokenResponse = microsoftAccountService.getTokenFromAuthCode(code, idTokenObj.getTenantId());
                session.setAttribute("tokens", tokenResponse);
                session.setAttribute("userConnected", true);
                session.setAttribute("userName", idTokenObj.getName());
                session.setAttribute("userTenantId", idTokenObj.getTenantId());
            } else {
                session.setAttribute("error", "ID token failed validation.");
            }
        } else {
            session.setAttribute("error", "Unexpected state returned from authority.");
        }
        // TODO
        return "done";
    }
}
