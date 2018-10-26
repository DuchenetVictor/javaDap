package fr.ynov.dap.dap.web;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.ynov.dap.dap.google.service.GMailService;

/**
 *
 * @author David_tepoche
 *
 */
@Controller
public class Welcome {
    /**
     * link gmailservice.
     */
    @Autowired
    private GMailService gMailService;

    /**
     * .
     *
     * @return dunno
     * @throws IOException
     * @throws GeneralSecurityException
     */
    @RequestMapping("/")
    public String Welcome(ModelMap map) throws GeneralSecurityException, IOException {

        Integer nbunreadEmails = gMailService.nbrEmailUnread("victor");

        map.addAttribute("nbEmails", nbunreadEmails);
        return "Welcome";
    }

}
