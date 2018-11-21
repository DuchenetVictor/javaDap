package fr.ynov.dap.dap.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.ynov.dap.dap.data.AppUser;
import fr.ynov.dap.dap.data.AppUserRepostory;
import fr.ynov.dap.dap.data.GoogleAccount;
import fr.ynov.dap.dap.data.MicrosoftAccount;
import fr.ynov.dap.dap.google.service.GMailService;
import fr.ynov.dap.dap.microsoft.services.MicrosoftMailService;

/**
 *
 * @author David_tepoche
 *
 */
@RestController
@RequestMapping("/emails")
public class MailController extends BaseController {

    /**
     * dunno.
     */
    @Autowired
    private GMailService gmailService;

    /**
     * dunno.
     */
    @Autowired
    private MicrosoftMailService microsoftMailService;

    /**
     * link appUser repository.
     */
    @Autowired
    private AppUserRepostory appUserRepository;

    /**
     * get the number of mail unread.
     *
     * @param userKey user key.
     * @return number of gmail unread.
     * @throws IOException              throw from gmailService
     * @throws GeneralSecurityException throw from gmailService
     */
    @RequestMapping("/nbrunreadmail/{userKey}")
    public @ResponseBody Object getNbrUnreadMail(@PathVariable("userKey") final String userKey)
            throws GeneralSecurityException, IOException {
        Integer nbrEmailUnread = 0;

        AppUser appUser = appUserRepository.findByUserKey(userKey);
        if (appUser == null) {
            getLogger().warn("Utilisateur non present en bdd: " + userKey);
            throw new NullPointerException("Utilisateur non present en base de donnée");
        }

        for (GoogleAccount account : appUser.getgAccounts()) {
            nbrEmailUnread += gmailService.nbrEmailUnread(account.getAccountName());
        }
        for (MicrosoftAccount account : appUser.getmAccounts()) {
            nbrEmailUnread += microsoftMailService.nbrMailUnread(account);
        }

        return nbrEmailUnread;
    }

    @Override
    public final String getClassName() {
        // TODO Auto-generated method stub
        return MailController.class.getName();
    }
}
