package dap.client.model.dto;

import java.util.List;

public class UserResponse {

    private Integer id;
    private String userKey;
    private List<GoogleAccountResponse> accounts;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the userKey
     */
    public String getUserKey() {
        return userKey;
    }

    /**
     * @param userKey the userKey to set
     */
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    /**
     * @return the accounts
     */
    public List<GoogleAccountResponse> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<GoogleAccountResponse> accounts) {
        this.accounts = accounts;
    }

}
