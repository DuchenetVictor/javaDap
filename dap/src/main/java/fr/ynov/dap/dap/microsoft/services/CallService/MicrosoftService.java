package fr.ynov.dap.dap.microsoft.services.CallService;

import javax.xml.ws.RespectBinding;

import fr.ynov.dap.dap.model.MicrosoftContact;
import fr.ynov.dap.dap.model.MicrosoftEvent;
import fr.ynov.dap.dap.model.MicrosoftMail;
import fr.ynov.dap.dap.model.MicrosoftMailFolder;
import fr.ynov.dap.dap.model.PagedResult;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MicrosoftService {

	
    @GET("/v1.0/me/events")
    Call<PagedResult<MicrosoftEvent>> getEvents(@Query("$orderby") String orderBy, @Query("$filter") String filter,
            @Query("$select") String select, @Query("$top") Integer maxResults);
    
    /**
     * Get contacts from Microsoft Graph API.
     * @param orderBy Order for result
     * @param select Which information to retrieve.
     * @return List of contacts
     */
    @GET("/v1.0/me/contacts")
    Call<PagedResult<MicrosoftContact>> getContacts(@Query("$orderby") String orderBy, @Query("$select") String select);
    
    /**
	 * Get details for a specific folder.
	 * 
	 * @param folderName Folder name
	 * @return Outlook folder.
	 */
	@GET("/v1.0/me/mailFolders/{folderName}")
	Call<MicrosoftMailFolder> getFolder(@Path("folderName") String folderName);

	/**
	 * Get Message (paginated).
	 * 
	 * @param folderId   Folder id
	 * @param orderBy    Order By
	 * @param select     Select
	 * @param maxResults Max results
	 * @return Messages
	 */
	@GET("/v1.0/me/mailfolders/{folderid}/messages")
	Call<PagedResult<MicrosoftMail>> getMessages(@Path("folderid") String folderId, @Query("$orderby") String orderBy,
			@Query("$select") String select, @Query("$top") Integer maxResults);
	
	@GET("/v1.0/me/contacts/$count")
	Call<ResponseBody> getNbrContact();
}
