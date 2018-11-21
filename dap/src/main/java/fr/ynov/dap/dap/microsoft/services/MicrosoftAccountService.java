package fr.ynov.dap.dap.microsoft.services;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import fr.ynov.dap.dap.SecretFileAccesException;
import fr.ynov.dap.dap.data.AppUser;
import fr.ynov.dap.dap.data.AppUserRepostory;
import fr.ynov.dap.dap.data.MicrosoftAccount;
import fr.ynov.dap.dap.microsoft.services.CallService.TokenService;
import fr.ynov.dap.dap.model.TokenResponse;

/**
 *
 * @author David_tepoche
 *
 */
@Service
public class MicrosoftAccountService extends MicrosoftBaseService {

    /**
     * link the AppUserRepostory.
     */
    @Autowired
    private AppUserRepostory appUserRepostory;

    /**
     * retrieve token from authCode.
     *
     * @param authCode dunno
     * @param tenantId dunno
     * @return the tokenResponse
     * @throws SecretFileAccesException dunno
     */
    public TokenResponse getTokenFromAuthCode(final String authCode, final String tenantId)
            throws SecretFileAccesException {

        // Generate the token service
        TokenService tokenService = getRetrofit().create(TokenService.class);

        try {
            TokenResponse tokenResponse = tokenService.getAccessTokenFromAuthCode(tenantId, getAppId(),
                    getAppPassword(), "authorization_code", authCode, getRedirectUrl()).execute().body();

            return ensureTokens(tokenResponse.getExpirationTime(), tokenResponse.getRefreshToken(),
                    tokenResponse.getAccessToken(), tenantId);

        } catch (IOException e) {
            getLogger().error("Probleme lors de la recuperation du token", e);
            TokenResponse error = new TokenResponse();
            return error;
        }
    }

    /**
     * dunno.
     *
     * @param state dunno
     * @param nonce dunno
     * @return dunno
     * @throws SecretFileAccesException ..
     */
    public String getLoginUrl(final UUID state, final UUID nonce) throws SecretFileAccesException {

        String url = getConfig().getMicrosoftAuthorityUrl() + getConfig().getMicrosoftRootUrlCallBack();
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url);
        urlBuilder.queryParam("client_id", getAppId());
        urlBuilder.queryParam("redirect_uri", getRedirectUrl());
        urlBuilder.queryParam("response_type", "code id_token");
        urlBuilder.queryParam("scope", getScopes());
        urlBuilder.queryParam("state", state);
        urlBuilder.queryParam("nonce", nonce);
        urlBuilder.queryParam("response_mode", "form_post");

        return urlBuilder.toUriString();
    }

    @Override
    public final String getClassName() {
        return MicrosoftBaseService.class.getName();
    }

    /**
     * link the microsoft account with the userKey in bdd.
     *
     * @param accountName dunno
     * @param userKey     dunno
     * @param request     dunno
     * @param session     dunno
     * @param response    dunno
     * @param state       dunno
     * @param nonce       dunno
     * @throws IOException              dunno
     * @throws SecretFileAccesException dunno
     */
    public void addAccount(final String accountName, final String userKey, final HttpServletRequest request,
            final HttpSession session, final HttpServletResponse response, final UUID state, final UUID nonce)
            throws IOException, SecretFileAccesException {
        AppUser appUser = appUserRepostory.findByUserKey(userKey);

        if (appUser == null) {
            getLogger().warn("Ajout d'un compte pour un utilisateur non present en bdd: " + userKey);
            throw new NullPointerException("Utilisateur non present en base de donnée");
        }
        MicrosoftAccount microsoftAccount = appUser.getmAccounts().stream()
                .filter(ma -> ma.getAccountName().equalsIgnoreCase(accountName)).findFirst().orElse(null);
        if (microsoftAccount != null) {
            getLogger().warn("Ajout d'un utilistaeur deja present en Bdd: " + microsoftAccount.getAccountName());

            TokenResponse tokenResponse = ensureTokens(microsoftAccount.getExpirationDate(),
                    microsoftAccount.getRefreshToken(), microsoftAccount.getAccessToken(),
                    microsoftAccount.getTenantId());
            microsoftAccount.setAccessToken(tokenResponse.getAccessToken());
            microsoftAccount.setExpirationDate(tokenResponse.getExpiresIn());
            microsoftAccount.setTokenType(tokenResponse.getTokenType());
            microsoftAccount.setRefreshToken(tokenResponse.getRefreshToken());

            appUserRepostory.save(appUser);
        } else {
            session.setAttribute("accountName", accountName);
            session.setAttribute("userKey", userKey);

            String loginUrl = null;
            loginUrl = getLoginUrl(state, nonce);
            response.sendRedirect(loginUrl);
        }
    }

    /**
     * save the new accountName in userkey.
     *
     * @param tokenResponse dunno
     * @param userKey       dunno
     * @param accountName   dunno
     * @param tenantId      dunno
     */
    public void saveNewAccountNameInUserKey(final TokenResponse tokenResponse, final String userKey,
            final String accountName, final String tenantId) {
        AppUser appUser = appUserRepostory.findByUserKey(userKey);
        if (appUser != null) {
            MicrosoftAccount account = new MicrosoftAccount();
            account.setAccountName(accountName);
            account.setAccessToken(tokenResponse.getAccessToken());
            account.setRefreshToken(tokenResponse.getRefreshToken());
            account.setExpirationDate(tokenResponse.getExpiresIn());
            account.setTenantId(tenantId);
            appUser.addMicrosoftAccount(account);

            appUserRepostory.save(appUser);
        } else {
            throw new NullPointerException("The userKey isn't exist in DB : " + userKey);
        }
    }
}
