package dap.client.model.dto;

import java.util.List;

public class UserResponse {

    private Integer id;
    private String userKey;
    private List<GoogleAccountResponse> googleAccountResponses;

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
     * @return the googleAccountResponses
     */
    public List<GoogleAccountResponse> getGoogleAccountResponses() {
        return googleAccountResponses;
    }

    /**
     * @param googleAccountResponses the googleAccountResponses to set
     */
    public void setGoogleAccountResponses(List<GoogleAccountResponse> googleAccountResponses) {
        this.googleAccountResponses = googleAccountResponses;
    }

}
