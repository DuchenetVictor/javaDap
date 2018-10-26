package fr.ynov.dap.dap.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.ynov.dap.dap.data.AppUser;
import fr.ynov.dap.dap.data.AppUserRepostory;
import fr.ynov.dap.dap.data.GoogleAccount;
import fr.ynov.dap.dap.google.ContactService;

/**
 *
 * @author David_tepoche
 *
 */
@RestController
@RequestMapping("/contact")
public class ContactController extends BaseController {

    /**
     * link with contactService.
     */
    @Autowired
    private ContactService contactService;

    /**
     * link appUser repository.
     */
    @Autowired
    private AppUserRepostory appUserRepository;

    /**
     * get the number of contact from userId.
     *
     * @param userKey user token
     * @return number of contact
     * @throws GeneralSecurityException throw if the contactService fail
     * @throws IOException              throw if the contactService fail
     */
    @GetMapping("/nbrContact/{userKey}")
    public @ResponseBody Integer nbrOfContact(@PathVariable("userKey") final String userKey)
            throws IOException, GeneralSecurityException {

        AppUser appUser = appUserRepository.findByUserKey(userKey);
        if (appUser == null) {
            getLogger().warn("Utilisateur non present en bdd: " + userKey);
            throw new NullPointerException("Utilisateur non present en base de donnée");
        }
        Integer nbrContact = 0;
        for (GoogleAccount account : appUser.getAccounts()) {
            nbrContact += contactService.getNbrContact(account.getAccountName());
        }
        return nbrContact;
    }

    /**
     * return the name of the current class.
     */
    @Override
    public String getClassName() {
        return ContactController.class.getName();
    }

}
