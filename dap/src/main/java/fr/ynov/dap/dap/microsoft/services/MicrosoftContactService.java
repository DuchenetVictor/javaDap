package fr.ynov.dap.dap.microsoft.services;

import java.io.IOException;

import org.springframework.stereotype.Service;

import fr.ynov.dap.dap.data.MicrosoftAccount;
import fr.ynov.dap.dap.exception.SecretFileAccesException;
import fr.ynov.dap.dap.microsoft.services.MicrosoftMailService;
import fr.ynov.dap.dap.model.MicrosoftContact;
import fr.ynov.dap.dap.model.PagedResult;
import fr.ynov.dap.dap.model.TokenResponse;



@Service
public class MicrosoftContactService extends MicrosoftBaseService{

	
	public int getNbrContact(MicrosoftAccount microsoftAccount) throws IOException, SecretFileAccesException {
		Object  nbrOfContacts = getMicrosoftService(microsoftAccount).getNbrContact().execute().body();
		
		return 0;
	}

	@Override
	protected final String getClassName() {
		return MicrosoftMailService.class.getName();
	}
	
}
