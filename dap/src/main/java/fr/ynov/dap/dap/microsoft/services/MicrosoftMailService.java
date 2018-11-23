package fr.ynov.dap.dap.microsoft.services;

import org.springframework.beans.factory.annotation.Autowired;

import fr.ynov.dap.dap.data.AppUserRepostory;
import fr.ynov.dap.dap.data.MicrosoftAccount;
import fr.ynov.dap.dap.microsoft.services.CallService.MailService;

/**
 *
 * @author David_tepoche
 *
 */
public class MicrosoftMailService extends MicrosoftBaseService {

    @Autowired
    private AppUserRepostory appUserRepostory;

    public int nbrMailUnread(MicrosoftAccount microsoftAccount) {
        // Generate the token service
        MicrosoftAccountService.

                MailService mailService = getRetrofit().create(MailService.class);

        return 0;

    }

    @Override
    protected final String getClassName() {
        return MicrosoftMailService.class.getName();
    }

}
