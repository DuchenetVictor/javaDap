package fr.ynov.dap.dap.model;

/**
 *
 * @author David_tepoche
 *
 */
public class StoredCredentialsResponse {

    /**
     * user.
     */
    private String user;
    /**
     * storedCredential Of the user.
     */
    private String storedCredential;

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param usr the user to set
     */
    public void setUser(final String usr) {
        this.user = usr;
    }

    /**
     * @return the storedCredential
     */
    public String getStoredCredential() {
        return storedCredential;
    }

    /**
     * @param strcred the storedCredential to set
     */
    public void setStoredCredential(final String strcred) {
        this.storedCredential = strcred;
    }

}
