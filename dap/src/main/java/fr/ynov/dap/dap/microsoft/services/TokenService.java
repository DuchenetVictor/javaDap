package fr.ynov.dap.dap.microsoft.services;

import com.google.api.client.auth.oauth2.TokenResponse;

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
public interface TokenService {
    /**
     * call the auth2 for the microsoft token.
     *
     * @param tenantId     dunno
     * @param clientId     dunno
     * @param clientSecret dunno
     * @param grantType    dunno
     * @param code         dunno
     * @param redirectUrl  dunno
     * @return return the token response from microsoft
     */
    @FormUrlEncoded
    @POST("/{tenantid}/oauth2/v2.0/token")
    Call<TokenResponse> getAccessTokenFromAuthCode(@Path("tenantid") String tenantId,
            @Field("client_id") String clientId, @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType, @Field("code") String code,
            @Field("redirect_uri") String redirectUrl);

    /**
     * dunno.
     *
     * @param tenantId     dunno
     * @param clientId     dunno
     * @param clientSecret dunno
     * @param grantType    dunno
     * @param code         dunno
     * @param redirectUrl  dunno
     * @return dunno
     */
    @FormUrlEncoded
    @POST("/{tenantid}/oauth2/v2.0/token")
    Call<TokenResponse> getAccessTokenFromRefreshToken(@Path("tenantid") String tenantId,
            @Field("client_id") String clientId, @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType, @Field("refresh_token") String code,
            @Field("redirect_uri") String redirectUrl);

}
