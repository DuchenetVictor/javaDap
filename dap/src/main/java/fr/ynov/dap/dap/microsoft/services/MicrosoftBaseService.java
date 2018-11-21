package fr.ynov.dap.dap.microsoft.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import fr.ynov.dap.dap.Config;
import fr.ynov.dap.dap.SecretFileAccesException;
import fr.ynov.dap.dap.microsoft.services.CallService.TokenService;
import fr.ynov.dap.dap.model.TokenResponse;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 *
 * @author David_tepoche
 *
 */
abstract class MicrosoftBaseService {

    /**
     * the name of the properties in file.
     */
    private static final String REDIRECT_URL = "redirectUrl";

    /**
     * the name of the properties in file.
     */
    private static final String APP_PASSWORD = "appPassword";

    /**
     * the name of the properties in file.
     */
    private static final String APP_ID = "appId";

    /**
     * link the config.
     */
    @Autowired
    private Config config;

    /**
     * stock the instance,it is not usefull to read each time the client'secret
     * file.
     */
    private static Properties clientProperties;

    /**
     * Initialize the logger.
     */
    private Logger logger = LogManager.getLogger(getClassName());

    /**
     * list of all the scope required for the appli.
     */
    private static final List<String> SCOPES = Arrays.asList("openid", "offline_access", "profile", "User.Read",
            "Mail.Read");

    /**
     * get the name of the class.
     *
     * @return the name of the class
     */
    protected abstract String getClassName();

    /**
     * @return the scopes
     */
    protected static String getScopes() {
        StringBuilder sb = new StringBuilder();
        for (String scope : SCOPES) {
            sb.append(scope + " ");
        }
        return sb.toString().trim();
    }

    /**
     * @return the config
     */
    protected Config getConfig() {
        return config;
    }

    /**
     * get properties from microsoft properties client' secret.
     *
     * @return Porperties with all the info from the file with the client'secret
     * @throws SecretFileAccesException dunno.
     */
    private Properties getClientProperties() throws SecretFileAccesException {
        if (clientProperties == null) {
            clientProperties = new Properties();
            try (FileInputStream istreamClientSecretFile = new FileInputStream(
                    config.getDataStoreDirectory() + File.separator + config.getMicrosoftClientSecretFile())) {
                if (istreamClientSecretFile != null) {
                    clientProperties.load(istreamClientSecretFile);
                }
            } catch (IOException e) {
                throw new SecretFileAccesException(e);
            }
        }
        return clientProperties;
    }

    /**
     * dunno .
     *
     * @param expirationDate dunno
     * @param refreshToken   dunno
     * @param accesToken     dunno
     * @param tenantId       dunno
     * @return dunno
     * @throws IOException              dunno
     * @throws SecretFileAccesException dunno
     */
    public TokenResponse ensureTokens(final Date expirationDate, final String refreshToken, final String accesToken,
            final String tenantId) throws IOException, SecretFileAccesException {
        // Are tokens still valid?
        Calendar now = Calendar.getInstance();
        if (now.getTime().before(expirationDate)) {
            // Still valid, return them as-is
            TokenResponse tokens = new TokenResponse();
            tokens.setAccessToken(accesToken);

            tokens.setExpirationTime(expirationDate);
            tokens.setRefreshToken(refreshToken);
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
                    refreshToken, getRedirectUrl()).execute().body();
        }
    }

    /**
     * dunno.
     *
     * @param accessToken dunno
     * @return dunno
     */
    protected Retrofit getRetrofit(final String accessToken) {
        // Create a logging interceptor to log request and responses
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        Interceptor requestInterceptor = new Interceptor() {
            @Override
            public Response intercept(final Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                okhttp3.Request.Builder builder = original.newBuilder().header("User-Agent", "java-tutorial")
                        .header("client-request-id", UUID.randomUUID().toString())
                        .header("return-client-request-id", "true")
                        .header("Authorization", String.format("Bearer %s", accessToken))
                        .method(original.method(), original.body());

                Request request = builder.build();
                return chain.proceed(request);
            }
        };

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(requestInterceptor).addInterceptor(interceptor)
                .build();

        // Create and configure the Retrofit object
        return new Retrofit.Builder().baseUrl(config.getMicrosoftAuthorityUrl()).client(client)
                .addConverterFactory(JacksonConverterFactory.create()).build();

        // TODO mettre le ensureTOken ?
    }

    /**
     * dunno.
     *
     * @return redirect url from client'secret file
     * @throws SecretFileAccesException dunno
     */
    protected String getRedirectUrl() throws SecretFileAccesException {
        return getClientProperties().getProperty(REDIRECT_URL);
    }

    /**
     * dunno.
     *
     * @return password from client'secret file
     * @throws SecretFileAccesException dunno
     */
    protected String getAppPassword() throws SecretFileAccesException {
        return getClientProperties().getProperty(APP_PASSWORD);
    }

    /**
     * dunno.
     *
     * @return app id from client'secret file
     * @throws SecretFileAccesException dunno
     */
    protected String getAppId() throws SecretFileAccesException {
        return getClientProperties().getProperty(APP_ID);
    }

    /**
     * @return the logger
     */
    protected Logger getLogger() {
        return logger;
    }
}
