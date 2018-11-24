package fr.ynov.dap.dap.microsoft.services;

import java.io.IOException;

import org.springframework.stereotype.Service;

import fr.ynov.dap.dap.data.MicrosoftAccount;
import fr.ynov.dap.dap.exception.SecretFileAccesException;
import fr.ynov.dap.dap.microsoft.services.MicrosoftMailService;
import okhttp3.ResponseBody;

@Service
public class MicrosoftContactService extends MicrosoftBaseService {

	public Integer getNbrContact(MicrosoftAccount microsoftAccount) throws IOException, SecretFileAccesException {
		// get the reponseBody because MS send only the nomber of contact without json
		// etc ...
		ResponseBody e = getMicrosoftService(microsoftAccount).getNbrContact().execute().body();
		if (e != null && !e.string().isEmpty()) {
			Integer nbrContact = (Integer.valueOf(e.string()));
			return nbrContact;
		} else {
			getLogger().error("le count des contact microsoft a renvoyé une reponse null" + microsoftAccount.getAccountName());
			return 0;
		}
	}

	@Override
	protected final String getClassName() {
		return MicrosoftMailService.class.getName();
	}
}
