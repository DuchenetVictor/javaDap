package fr.ynov.dap.dap.google;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;

import fr.ynov.dap.dap.Config;
import fr.ynov.dap.dap.data.AppUser;
import fr.ynov.dap.dap.data.AppUserRepostory;

/**
 *
 * @author David_tepoche
 *
 */
@Service
public class GoogleAccount extends BaseService {

    /**
     * link config.
     */
    @Autowired
    private Config config;

    /**
     * link the appUser repository.
     */
    @Autowired
    private AppUserRepostory userAppRepository;

    /**
     * Handle the Google response.
     *
     * @param request The HTTP Request
     * @param code    The (encoded) code use by Google (token, expirationDate,...)
     * @param session the HTTP Session
     * @return the view to display
     * @throws ServletException When Google account could not be connected to DaP.
     */

    public String oAuthCallback(final String code, final HttpServletRequest request, final HttpSession session)
            throws ServletException {
        final String decodedCode = extracCode(request);

        final String redirectUri = buildRedirectUri(request, new Config().getoAuth2CallbackUrl());

        final String accountName = session.getAttribute("accountName").toString();
        final String userKey = session.getAttribute("userKey").toString();
        try {
            final GoogleAuthorizationCodeFlow flow = super.getFlow();
            final TokenResponse response = flow.newTokenRequest(decodedCode).setRedirectUri(redirectUri).execute();

            final Credential credential = flow.createAndStoreCredential(response, accountName);
            if (null == credential || null == credential.getAccessToken()) {
                getLogger().warn("Trying to store a NULL AccessToken for user : " + accountName);
            }

            if (getLogger().isDebugEnabled() && null != credential && null != credential.getAccessToken()) {
                getLogger().debug("New user credential stored with userId : " + accountName + "partial AccessToken : "
                        + credential.getAccessToken().toString());

            }
            // onSuccess(request, resp, credential);
        } catch (IOException | GeneralSecurityException e) {
            getLogger().error("Exception while trying to store user Credential", e);
            throw new ServletException("Error while trying to conenct Google Account");
        }

        AppUser appUser = userAppRepository.findByUserKey(userKey);
        fr.ynov.dap.dap.data.GoogleAccount account = new fr.ynov.dap.dap.data.GoogleAccount();
        account.setAccountName(accountName);
        appUser.addGoogleAccount(account);

        return "Vous etes Connecté !";
    }

    /**
     * Extract OAuth2 Google code (from URL) and decode it.
     *
     * @param request the HTTP request to extract OAuth2 code
     * @return the decoded code
     * @throws ServletException if the code cannot be decoded
     */
    private String extracCode(final HttpServletRequest request) throws ServletException {
        final StringBuffer buf = request.getRequestURL();
        if (null != request.getQueryString()) {
            buf.append('?').append(request.getQueryString());
        }
        final AuthorizationCodeResponseUrl responseUrl = new AuthorizationCodeResponseUrl(buf.toString());
        final String decodeCode = responseUrl.getCode();

        if (decodeCode == null) {
            throw new MissingServletRequestParameterException("code", "String");
        }

        if (null != responseUrl.getError()) {
            getLogger().error("Error when trying to add Google acocunt : " + responseUrl.getError());
            throw new ServletException("Error when trying to add Google acocunt");
            // onError(request, resp, responseUrl);
        }

        return decodeCode;
    }

    /**
     * Build a current host (and port) absolute URL.
     *
     * @param req         The current HTTP request to extract schema, host, port
     *                    informations
     * @param destination the "path" to the resource
     * @return an absolute URI
     */
    protected String buildRedirectUri(final HttpServletRequest req, final String destination) {
        final GenericUrl url = new GenericUrl(req.getRequestURL().toString());
        url.setRawPath(destination);
        return url.build();
    }

    @Override
    protected final String getClassName() {
        return GoogleAccount.class.getName();
    }

    /**
     * Add a Google account (user will be prompt to connect and accept required
     * access).
     *
     * @param accountName  the user to store Data
     * @param request     the HTTP request
     * @param userKey     user in Bdd
     * @param httpSession dunno.
     * @return the view to Display (on Error)
     * @throws GeneralSecurityException dunno.
     * @throws IOException              dunno.
     */
    public String addAccount(final String accountName, final String userKey, final HttpServletRequest request,
            final HttpSession httpSession) throws GeneralSecurityException, IOException {
        Credential credential;
        GoogleAuthorizationCodeFlow flow;
        String stringReturn = "";

        if (userAppRepository.findByUserKey(userKey) == null) {
            getLogger().warn("ajout d'un compte pour un utilisateur non present en bdd: " + userKey);
            throw new NullPointerException("Utilisateur non present en base de donnée");
        }

        flow = super.getFlow();
        credential = flow.loadCredential(accountName);

        if (credential != null && credential.getAccessToken() != null) {
            AppUser appUser = userAppRepository.findByUserKey(userKey);
            Boolean accountAlreadyAdd = appUser.getAccounts().stream()
                    .anyMatch(a -> a.getAccountName().equalsIgnoreCase(accountName));
            if (!accountAlreadyAdd) {
                fr.ynov.dap.dap.data.GoogleAccount gAccount = new fr.ynov.dap.dap.data.GoogleAccount();
                gAccount.setAccountName(accountName);

                appUser.addGoogleAccount(gAccount);
                userAppRepository.save(appUser);
            }

        } else {
            // redirect to the authorization flow
            final AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
            authorizationUrl.setRedirectUri(buildRedirectUri(request, config.getoAuth2CallbackUrl()));

            // store userKey and accountName in session for CallBack Access
            httpSession.setAttribute("accountName", accountName);
            httpSession.setAttribute("userKey", userKey);

            stringReturn = "redirect:" + authorizationUrl.build();
        }
        return stringReturn;

    }

}
