package fr.ynov.dap.dap.microsoft.services;

import java.io.IOException;
import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;

import fr.ynov.dap.dap.data.MicrosoftAccount;
import fr.ynov.dap.dap.exception.SecretFileAccesException;
import fr.ynov.dap.dap.model.MicrosoftEvent;
import fr.ynov.dap.dap.model.PagedResult;

@Service
public class MicrosoftEventService extends MicrosoftBaseService{

	public MicrosoftEvent[] getEvent(final MicrosoftAccount  microsoftAccount, String dateFrom, Integer maxResult) throws IOException, SecretFileAccesException {
		
		return getMicrosoftService(microsoftAccount).getEvents("start/dateTime DESC", "start/dateTime  GE '"+dateFrom +"'", null, maxResult).execute().body().getValue();
	}
	
	@Override
	protected String getClassName() {
		return MicrosoftEventService.class.getName();
	}
}
