package fr.ynov.dap.dap.microsoft.services;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.client.auth.oauth2.TokenResponse;

import fr.ynov.dap.dap.SecretFileAccesException;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 *
 * @author David_tepoche
 *
 */
@Service
public class MicrosoftAccountService extends MicrosoftBaseService {

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
        // Create a logging interceptor to log request and responses
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Create and configure the Retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getConfig().getMicrosoftAuthorityUrl()).client(client)
                .addConverterFactory(JacksonConverterFactory.create()).build();

        // Generate the token service
        TokenService tokenService = retrofit.create(TokenService.class);

        try {
            TokenResponse tokenResponse = tokenService.getAccessTokenFromAuthCode(tenantId, getAppId(),
                    getAppPassword(), "authorization_code", authCode, getRedirectUrl()).execute().body();

            return ensureTokens(tokenResponse, tenantId);

        } catch (IOException e) {
            getLogger().error("Probleme lors de la recuperation du token", e);
            TokenResponse error = new TokenResponse();
            return error;
        }
    }

    /**
     * dunno.
     *
     * @param tokens   dunno
     * @param tenantId dunno
     * @return dunno
     * @throws SecretFileAccesException dunno
     * @throws IOException              dunno
     */
    public TokenResponse ensureTokens(final TokenResponse tokens, final String tenantId)
            throws IOException, SecretFileAccesException {
        // Are tokens still valid?
        Calendar now = Calendar.getInstance();
        if (now.getTime().before(new Date(tokens.getExpiresInSeconds()))) {
            // Still valid, return them as-is
            return tokens;
        } else {
            // Expired, refresh the tokens
            // Create a logging interceptor to log request and responses
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            // Create and configure the Retrofit object
            Retrofit retrofit = new Retrofit.Builder().baseUrl(getConfig().getMicrosoftAuthorityUrl()).client(client)
                    .addConverterFactory(JacksonConverterFactory.create()).build();

            // Generate the token service
            TokenService tokenService = retrofit.create(TokenService.class);

            return tokenService.getAccessTokenFromRefreshToken(tenantId, getAppId(), getAppPassword(), "refresh_token",
                    tokens.getRefreshToken(), getRedirectUrl()).execute().body();
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
}
