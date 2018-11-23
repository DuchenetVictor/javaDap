package fr.ynov.dap.dap.microsoft.services.CallService;

import fr.ynov.dap.dap.model.TokenResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 *
 * @author David_tepoche
 *
 */
public interface MailService {

    /**
     * call the auth2 for the microsoft token.
     *
     * @param tenantId dunno
     * @param folderId dunno
     *
     * @return return the token response from microsoft
     */
    @FormUrlEncoded
    @POST("/{tenantid}/v2.0/me/mailfolders/{folderid}/messages")
    Call<TokenResponse> getAccessTokenFromAuthCode(@Path("tenantid") String tenantId,
            @Field("folderid") String folderId);

}
