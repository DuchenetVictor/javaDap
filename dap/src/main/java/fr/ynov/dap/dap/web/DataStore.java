package fr.ynov.dap.dap.web;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.api.client.auth.oauth2.StoredCredential;

import fr.ynov.dap.dap.data.AppUser;
import fr.ynov.dap.dap.data.AppUserRepostory;
import fr.ynov.dap.dap.data.GoogleAccount;
import fr.ynov.dap.dap.data.MicrosoftAccount;
import fr.ynov.dap.dap.google.service.UserInfoService;
import fr.ynov.dap.dap.microsoft.services.MicrosoftAccountService;
import fr.ynov.dap.dap.model.StoredCredentialsResponse;

/**
 *
 * @author David_tepoche
 *
 */
@Controller
public class DataStore {

	/**
	 * link userInfoService.
	 */
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private MicrosoftAccountService microsoftAccountService;

	@Autowired
	private AppUserRepostory appUSerRepository;

//	/**
//	 * dunno.
//	 *
//	 * @param map .
//	 * @return .
//	 * @throws IOException .
//	 * @throws GeneralSecurityException .
//	 */
//	@RequestMapping("/dataStore")
//	public String getDataStore(final ModelMap map) throws IOException, GeneralSecurityException {
//		com.google.api.client.util.store.DataStore<StoredCredential> s = userInfoService.getCredentialDataStore();
//		List<StoredCredentialsResponse> storedCredentialsResponses = new ArrayList<>();
//		for (String userId : s.keySet()) {
//			StoredCredentialsResponse storedCredentialsResponse = new StoredCredentialsResponse();
//			storedCredentialsResponse.setUser(userId);
//			storedCredentialsResponse
//					.setStoredCredential(s.get(userId).getAccessToken() + s.get(userId).getRefreshToken());
//			storedCredentialsResponses.add(storedCredentialsResponse);
//		}
//
//		map.addAttribute("users", storedCredentialsResponses);
//
//		return "storedCredentials";
//
//	}

	@GetMapping("/admin/{userKey}")
	public String getAdminPage(@PathVariable(value = "userKey") final String userKey,final ModelMap map) throws IOException, GeneralSecurityException {
		AppUser appUser = appUSerRepository.findByUserKey(userKey);
		List<StoredCredentialsResponse> storedCredentialsResponses = new ArrayList<>();
		for (MicrosoftAccount microsoftAccount : appUser.getmAccounts()) {
			storedCredentialsResponses.add(new StoredCredentialsResponse(microsoftAccount));
			
		}
		com.google.api.client.util.store.DataStore<StoredCredential> s = userInfoService.getCredentialDataStore();
		for(GoogleAccount  googleAccount :appUser.getgAccounts()) {
			storedCredentialsResponses.add(new StoredCredentialsResponse(googleAccount, s.get(googleAccount.getAccountName()).getAccessToken()));	
		}
		map.addAttribute("comptes", storedCredentialsResponses);
		
		return "storedCredentials";
	}

}
