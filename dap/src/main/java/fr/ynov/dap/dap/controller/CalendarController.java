package fr.ynov.dap.dap.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.mysql.fabric.xmlrpc.base.Array;

import fr.ynov.dap.dap.data.AppUser;
import fr.ynov.dap.dap.data.AppUserRepostory;
import fr.ynov.dap.dap.data.GoogleAccount;
import fr.ynov.dap.dap.data.MicrosoftAccount;
import fr.ynov.dap.dap.exception.SecretFileAccesException;
import fr.ynov.dap.dap.google.service.CalendarService;
import fr.ynov.dap.dap.google.service.UserInfoService;
import fr.ynov.dap.dap.microsoft.services.MicrosoftAccountService;
import fr.ynov.dap.dap.microsoft.services.MicrosoftEventService;
import fr.ynov.dap.dap.microsoft.services.CallService.MicrosoftService;
import fr.ynov.dap.dap.model.CalendarEvent;
import fr.ynov.dap.dap.model.MicrosoftEvent;

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

	@Autowired
	private MicrosoftEventService microsoftEventService;
	
	@Autowired
	private MicrosoftAccountService microsoftAccountService;

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
	 * @throws SecretFileAccesException
	 * @throws ParseException
	 */
	@GetMapping("/getNextEvent/{nbrEnvent}/{userKey}")
	public @ResponseBody List<CalendarEvent> getNextEvent(@PathVariable(value = "nbrEnvent") final String nbrEvent,
			@PathVariable("userKey") final String userKey) throws NumberFormatException, IOException,
			GeneralSecurityException, SecretFileAccesException, ParseException {

		AppUser appUser = appUserRepository.findByUserKey(userKey);
		if (appUser == null) {
			getLogger().warn("Utilisateur non present en bdd: " + userKey);
			throw new NullPointerException("Utilisateur non present en base de donnée");
		}
		Integer nbEventToDisplay = Integer.valueOf(nbrEvent);
		List<CalendarEvent> calendarEvents = new ArrayList<>();
		for (GoogleAccount account : appUser.getgAccounts()) {

			List<Event> lastEvents = calendarService.getLastEvent(nbEventToDisplay, account.getAccountName());

			String userEmail = userInfoService.getEmail(account.getAccountName());

			for (Event event : lastEvents) {
				calendarEvents.add(new CalendarEvent(event, userEmail));
			}
		}

		String dateNowFormatedAndUTC = getUTCDateNowformated();
		for (MicrosoftAccount account : appUser.getmAccounts()) {

			MicrosoftEvent[] events = microsoftEventService.getEvent(account, dateNowFormatedAndUTC, nbEventToDisplay);
			String email = microsoftAccountService.getUserEmail(account);
			for (MicrosoftEvent event : events) {
				calendarEvents.add(new CalendarEvent(event, email));
			}
		}

		calendarEvents.sort((a, b) -> a.getStartDate().compareTo(b.getStartDate()));

		return calendarEvents;
	}

	private String getUTCDateNowformated() {
		SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd");
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		String dateNowFormatedAndUTC = dateFormatUTC.format(new Date());
		return dateNowFormatedAndUTC;
	}

	@Override
	public final String getClassName() {
		return CalendarController.class.getName();
	}
}
