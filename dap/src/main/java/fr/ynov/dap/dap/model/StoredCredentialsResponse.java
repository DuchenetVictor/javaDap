package fr.ynov.dap.dap.model;

import fr.ynov.dap.dap.data.GoogleAccount;
import fr.ynov.dap.dap.data.MicrosoftAccount;
import fr.ynov.dap.dap.enums.AccountTypeEnum;

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
     * type of account
     */
    private AccountTypeEnum accountTypeEnum;
    
    /**
     * tenant id (only for microsoft Account)
     */
    private String tenantId;

    public StoredCredentialsResponse(GoogleAccount googleAccount, String token) {
		this.user = googleAccount.getAccountName();
		this.storedCredential = token;
		this.tenantId = null;
		this.accountTypeEnum = AccountTypeEnum.GOOGLE;
	}
    
    
    public StoredCredentialsResponse(MicrosoftAccount microsoftAccount) {
		this.user = microsoftAccount.getAccountName();
		this.storedCredential = microsoftAccount.getAccessToken();
		this.tenantId = microsoftAccount.getTenantId();
		this.accountTypeEnum = AccountTypeEnum.MICROSOFT;
	}

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

	/**
	 * @return the accountTypeEnum
	 */
	public AccountTypeEnum getAccountTypeEnum() {
		return accountTypeEnum;
	}

	/**
	 * @param accountTypeEnum the accountTypeEnum to set
	 */
	public void setAccountTypeEnum(AccountTypeEnum accountTypeEnum) {
		this.accountTypeEnum = accountTypeEnum;
	}

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
