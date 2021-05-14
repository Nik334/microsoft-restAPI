package com.ms.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsoft.aad.msal4j.Prompt;
import com.microsoft.graph.models.DateTimeTimeZone;
import com.ms.BasicConfiguration;
import com.ms.model.CalendarDetails;
import com.ms.model.CalendarEvent;
import com.ms.model.EventPostObject;
import com.ms.model.MsAttendee;
import com.ms.model.MsDate2;
import com.ms.model.ProfileResponse;
import com.ms.model.RefreshTokenResponse;
import com.ms.model.TokenResponse;

@RestController
public class MicrosoftHttpController {

	@Autowired
	private BasicConfiguration msConfig;

	@Autowired
	private RestTemplate restTemplate;

	private static final String SCOPE = "openid https://graph.microsoft.com/User.Read https://graph.microsoft.com/Calendars.ReadWrite";

	@GetMapping("/request")
	public String authorizeRequest() throws URISyntaxException {
		URI uri = new URIBuilder(msConfig.getAuthority() + "oauth2/v2.0/authorize")
				.addParameter("client_id", msConfig.getClientId()).addParameter("response_type", "code")
				.addParameter("redirect_uri", msConfig.getRedirectUriGraph()).addParameter("response_mode", "query")
				.addParameter("scope", "offline_access " + SCOPE).addParameter("state", "12345")
				.addParameter("prompt", Prompt.SELECT_ACCOUNT.toString()).build();

		return uri.toString();
	}

	@GetMapping("/myapp")
	public ResponseEntity<?> getToken(@RequestParam("code") String code) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("client_id", msConfig.getClientId());
		map.add("scope", SCOPE);
		map.add("grant_type", "authorization_code");
		map.add("redirect_uri", msConfig.getRedirectUriGraph());
		map.add("client_secret", msConfig.getSecretKey());
		map.add("code", code);

		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);

		ResponseEntity<TokenResponse> responseEntity = restTemplate
				.postForEntity(msConfig.getAuthority() + "oauth2/v2.0/token", entity, TokenResponse.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			return responseEntity;
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
		}
	}

	@GetMapping("/myProfile")
	public ResponseEntity<?> getProfile(@RequestParam("accessToken") String accessToken) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(headers);

		ResponseEntity<ProfileResponse> responseEntity = restTemplate.exchange(
				msConfig.getMsGraphEndpointHost() + "v1.0/me/", HttpMethod.GET, entity, ProfileResponse.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			return responseEntity;
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
		}
	}

	@GetMapping("/refreshToken")
	public ResponseEntity<?> getRefreshToken(@RequestParam("refreshToken") String refreshToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("client_id", msConfig.getClientId());
		map.add("scope", SCOPE);
		map.add("grant_type", "refresh_token");
		map.add("redirect_uri", msConfig.getRedirectUriGraph());
		map.add("client_secret", msConfig.getSecretKey());
		map.add("refresh_token", refreshToken);

		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);

		ResponseEntity<RefreshTokenResponse> responseEntity = restTemplate
				.postForEntity(msConfig.getAuthority() + "oauth2/v2.0/token", entity, RefreshTokenResponse.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			return responseEntity;
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
		}
	}

	@GetMapping("/calendar")
	public ResponseEntity<?> getCalendar(@RequestParam("accessToken") String accessToken) throws URISyntaxException {

//		?startDateTime=2021-04-01T12:01:23.802Z&endDateTime=2021-04-20T12:01:23.802Z1

		URI uri = new URIBuilder(msConfig.getMsGraphEndpointHost()
				+ "v1.0/me/calendarView/delta?startDateTime=2021-04-01T12:01:23.802Z&endDateTime=2021-04-20T12:01:23.802Z")
//				.addParameter("startDateTime", "2021-04-01T12:01:23.802Z")
//				.addParameter("endDateTime", "2021-04-20T12:01:23.802Z")
						.build();

		DateTimeTimeZone start = new DateTimeTimeZone();
		start.dateTime = new Date().toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(headers);

		ResponseEntity<CalendarEvent> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity,
				CalendarEvent.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			return responseEntity;
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
		}
	}

	@GetMapping("/skypeInterview")
	public ResponseEntity<?> scheduleSkypeInterview(@RequestParam("accessToken") String accessToken)
			throws URISyntaxException, JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);

		EventPostObject eventPostObject = setObject();

		HttpEntity<EventPostObject> entity = new HttpEntity<>(eventPostObject, headers);

		ResponseEntity<CalendarDetails> responseEntity = restTemplate
				.postForEntity(msConfig.getMsGraphEndpointHost() + "v1.0/me/events", entity, CalendarDetails.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			return responseEntity;
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
		}
	}

	private EventPostObject setObject() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		EventPostObject eventPostObject = new EventPostObject();
		eventPostObject.toEntry("Let's go for lunch", "teamsForBusiness");

		Date startDate = DateUtils.addHours(new Date(), -10);
		String startDateFormat = sdf.format(startDate);
		System.out.println("startDate => " + startDateFormat);
		eventPostObject.setStart(new MsDate2(startDateFormat));

		Date endDate = DateUtils.addHours(new Date(), -9);
		String endDateFormat = sdf.format(endDate);
		System.out.println("endDate => " + endDateFormat);
		eventPostObject.setEnd(new MsDate2(endDateFormat));

		eventPostObject.setAttendees(
				Collections.singletonList(new MsAttendee("nikhil@inogit.com", "Nikhil kumar", "required")));
		return eventPostObject;
	}

}
