package fr.ynov.dap.dap.microsoft.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import fr.ynov.dap.dap.Config;
import fr.ynov.dap.dap.exception.SecretFileAccesException;

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
