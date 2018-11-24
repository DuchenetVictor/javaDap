package fr.ynov.dap.dap.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.calendar.model.Event;

import fr.ynov.dap.dap.data.AppUser;
import fr.ynov.dap.dap.data.AppUserRepostory;
import fr.ynov.dap.dap.data.GoogleAccount;
import fr.ynov.dap.dap.google.service.CalendarService;
import fr.ynov.dap.dap.google.service.UserInfoService;
import fr.ynov.dap.dap.model.GoogleCalendarEvent;

/**
 *
 * @author David_tepoche
 *
 */
@RestController
@RequestMapping("/calendar")
public class CalendarController extends BaseController {

    /**
     * linkg config.
     */
    @Autowired
    private CalendarService calendarService;

    /**
     * link the UserinfoService.
     */
    @Autowired
    private UserInfoService userInfoService;

    /**
     * link app user repository.
     */
    @Autowired
    private AppUserRepostory appUserRepository;

    /**
     * dunno.
     *
     * @param nbrEvent .
     * @param userKey  user in bdd
     * @return list of CalendarEvent
     * @throws GeneralSecurityException throws by calendarService when it try to
     *                                  getCredential
     * @throws IOException              throws by userInfoSErvice or calendarService
     * @throws NumberFormatException    if the nbrEvent cannot be cast as an integer
     */
    @GetMapping("/getNextEvent/{nbrEnvent}/{userKey}")
    public @ResponseBody List<GoogleCalendarEvent> getNextEvent(@PathVariable(value = "nbrEnvent") final String nbrEvent,
            @PathVariable("userKey") final String userKey)
            throws NumberFormatException, IOException, GeneralSecurityException {

        AppUser appUser = appUserRepository.findByUserKey(userKey);
        if (appUser == null) {
            getLogger().warn("Utilisateur non present en bdd: " + userKey);
            throw new NullPointerException("Utilisateur non present en base de donnée");
        }

        List<GoogleCalendarEvent> calendarEvents = new ArrayList<>();
        for (GoogleAccount account : appUser.getgAccounts()) {
            List<Event> lastEvents = calendarService.getLastEvent(Integer.valueOf(nbrEvent), account.getAccountName());

            String userEmail = userInfoService.getEmail(account.getAccountName());

            for (Event event : lastEvents) {
                calendarEvents.add(new GoogleCalendarEvent(event, userEmail));
            }
        }
        return calendarEvents;
    }

    @Override
    public final String getClassName() {
        return CalendarController.class.getName();
    }
}
