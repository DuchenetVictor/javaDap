package fr.ynov.dap.dap.microsoft.services;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import fr.ynov.dap.dap.model.MicrosoftUserDetail;
import fr.ynov.dap.dap.model.TokenResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import fr.ynov.dap.dap.exception.SecretFileAccesException;
import fr.ynov.dap.dap.microsoft.services.CallService.TokenService;
import fr.ynov.dap.dap.data.AppUser;
import fr.ynov.dap.dap.data.AppUserRepostory;
import fr.ynov.dap.dap.data.MicrosoftAccount;

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

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

		// Create and configure the Retrofit object
		Retrofit retrofit = new Retrofit.Builder().baseUrl(getConfig().getMicrosoftAuthorityUrl()).client(client)
				.addConverterFactory(JacksonConverterFactory.create()).build();

		// Generate the token service
		TokenService tokenService = retrofit.create(TokenService.class);
		try {
			return tokenService.getAccessTokenFromAuthCode(tenantId, getAppId(), getAppPassword(), "authorization_code",
					authCode, getRedirectUrl()).execute().body();
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

	public String getUserEmail(final MicrosoftAccount microsoftAccount) throws IOException, SecretFileAccesException {
		MicrosoftUserDetail detail = getMicrosoftService(microsoftAccount).getUserDetails().execute().body();
		return getEmail(detail);
	}

	private String getEmail(MicrosoftUserDetail detail) {
		String errorMessage = "Impossible de recuperer les informations personnelles de l'utilisateur";
		if (detail == null) {

			getLogger().error("detail is null when trying to get userdetail");
			throw new NullPointerException(errorMessage);
		}

		String email;
		if (detail.getDisplayName() != null && detail.getDisplayName().contains("@")) {
			email = detail.getDisplayName();
		} else if (detail.getMail() != null) {
			email = detail.getMail();
		} else {
			throw new NullPointerException(errorMessage);
		}
		return email;
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
