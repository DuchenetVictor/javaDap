package fr.ynov.dap.dap;

/**
 *
 * @author David_tepoche
 *
 */
public class Config {
    /**
     * name of the application.
     */
    private static final String APPLICATION_NAME = "YnovDaP";
    /**
     * folder's name of the credentials.
     */
    private static final String CREDENTIALS_FOLDER_NAME = "token";
    /**
     * file's name of the for the google client' secret.
     */
    private static final String GOOGLE_SECRETS_CLIENT_FILE_NAME = "credentials.json";
    /**
     * file's name for microsoft client' secret.
     */
    private static final String MICROSOFT_SECRETS_CLIENT_FILE_NAME = "auth.properties";
    /**
     * root of the redirect url.
     */
    private static final String GOOGLE_ROOT_URL_REDIRECT = "/oAuth2Callback";
    /**
     * microsoft authority'url to login.
     */
    private static final String MICROSOFT_AUTHORITY_URL = "https://login.microsoftonline.com/";
    /**
     * url of the microsft redirect.
     */
    private static final String MICROSOFT_ROOT_URL_REDIRECT = "common/oauth2/v2.0/authorize";

    /**
     * store the oAuth2CallbackUrl.
     */
    private String googleRootUrlCallBack = GOOGLE_ROOT_URL_REDIRECT;
    /**
     * store the oAuth2CallbackUrl.
     */
    private String microsoftUrlCallBack = MICROSOFT_ROOT_URL_REDIRECT;
    /**
    *
    */
    private String microsoftClientSecretFile = MICROSOFT_SECRETS_CLIENT_FILE_NAME;
    /**
     * dunno.
     */
    private String microsoftAuthorityUrl = MICROSOFT_AUTHORITY_URL;
    /**
    *
    */
    private String googleClientSecretFile = GOOGLE_SECRETS_CLIENT_FILE_NAME;
    /**
    *
    */
    private String applicationName = APPLICATION_NAME;
    /**
    *
    */
    private String credentialsFolder = CREDENTIALS_FOLDER_NAME;
    /**
     * get the directory of the dataStore default = the home directory of the user.
     */
    private String dataStoreDirectory = System.getProperty("user.home");

    /**
    *
    */
    public Config() {
    }

    /**
     * @return the microsoftAuthorityUrl
     */
    public String getMicrosoftAuthorityUrl() {
        return microsoftAuthorityUrl;
    }

    /**
     * @param msAuthorityUrl the microsoftAuthorityUrl to set
     */
    public void setMicrosoftAuthorityUrl(final String msAuthorityUrl) {
        this.microsoftAuthorityUrl = msAuthorityUrl;
    }

    /**
     * @return the oAuth2CallbackUrl
     */
    public String getoAuth2CallbackUrl() {
        return googleRootUrlCallBack;
    }

    /**
     *
     * @param dataDirectory the path of the new directory to store the credential
     */
    public Config(final String dataDirectory) {
        this.dataStoreDirectory = dataDirectory;
    }

    /**
     * @return the dataStoreDirectory
     */
    public String getDataStoreDirectory() {
        return this.dataStoreDirectory;
    }

    /**
     * @param dataDirectory the dataStoreDirectory to set
     */
    public void setDataStoreDirectory(final String dataDirectory) {
        this.dataStoreDirectory = dataDirectory;
    }

    /**
     * @return the credentialsFolder
     */
    public String getCredentialsFolder() {
        return credentialsFolder;
    }

    /**
     * @return the microsoftClientSecretFile
     */
    public String getMicrosoftClientSecretFile() {
        return microsoftClientSecretFile;
    }

    /**
     * @return the microsoftRootUrlCallBack
     */
    public String getMicrosoftRootUrlCallBack() {
        return microsoftUrlCallBack;
    }

    /**
     * @param msUrlCallBack the microsoftRootUrlCallBack to set
     */
    public void setMicrosoftUrlCallBack(final String msUrlCallBack) {
        this.microsoftUrlCallBack = msUrlCallBack;
    }

    /**
     * @param msClientSecretFile the microsoftClientSecretFile to set
     */
    public void setMicrosoftClientSecretFile(final String msClientSecretFile) {
        microsoftClientSecretFile = microsoftClientSecretFile;
    }

    /**
     * @return the clientSecretDir
     */
    public String getGoogleClientSecretFile() {
        return this.googleClientSecretFile;
    }

    /**
     * @return the applicationName
     */
    public String getApplicationName() {
        return this.applicationName;
    }

}
