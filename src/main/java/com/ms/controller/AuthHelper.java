package com.ms.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.aad.msal4j.AuthorizationCodeParameters;
import com.microsoft.aad.msal4j.AuthorizationRequestUrlParameters;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.Prompt;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.ResponseMode;
import com.ms.BasicConfiguration;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class AuthHelper {

	private String clientId;
	private String clientSecret;
	private String authority;
	private String redirectUriGraph;
	private String msGraphEndpointHost;

	@Autowired
	BasicConfiguration configuration;

	@PostConstruct
	public void init() {
		clientId = configuration.getClientId();
		authority = configuration.getAuthority();
		clientSecret = configuration.getSecretKey();
		redirectUriGraph = configuration.getRedirectUriGraph();
		msGraphEndpointHost = configuration.getMsGraphEndpointHost();
	}

	String getAuthorizationCodeUrl(String claims, String scope, String registeredRedirectURL, String state,
			String nonce) throws MalformedURLException {

		String updatedScopes = scope == null ? "" : scope;

		PublicClientApplication pca = PublicClientApplication.builder(clientId).authority(authority).build();

		AuthorizationRequestUrlParameters parameters = AuthorizationRequestUrlParameters
				.builder(registeredRedirectURL, Collections.singleton(updatedScopes)).responseMode(ResponseMode.QUERY)
				.prompt(Prompt.SELECT_ACCOUNT).state(state).nonce(nonce).claimsChallenge(claims).build();

		return pca.getAuthorizationRequestUrl(parameters).toString();
	}

	public IAuthenticationResult getAuthResultByAuthCode(HttpServletRequest httpServletRequest,
			AuthorizationCode authorizationCode, String currentUri) throws Throwable {

		IAuthenticationResult result;
		ConfidentialClientApplication app;
		try {
			app = createClientApplication();

			String authCode = authorizationCode.getValue();
			AuthorizationCodeParameters parameters = AuthorizationCodeParameters.builder(authCode, new URI(currentUri))
					.build();

			Future<IAuthenticationResult> future = app.acquireToken(parameters);

			result = future.get();
		} catch (ExecutionException e) {
			throw e.getCause();
		}

		if (result == null) {
			throw new ServiceUnavailableException("authentication result was null");
		}

		System.out.println(app.tokenCache().serialize());

		return result;
	}

	private ConfidentialClientApplication createClientApplication() throws MalformedURLException {
		return ConfidentialClientApplication.builder(clientId, ClientCredentialFactory.createFromSecret(clientSecret))
				.authority(authority).build();
	}

	void processAuthenticationCodeRedirect(HttpServletRequest httpRequest, String currentUri, String fullUrl)
			throws Throwable {

		Map<String, List<String>> params = new HashMap<>();
		for (String key : httpRequest.getParameterMap().keySet()) {
			params.put(key, Collections.singletonList(httpRequest.getParameterMap().get(key)[0]));
		}

		AuthenticationResponse authResponse = AuthenticationResponseParser.parse(new URI(fullUrl), params);

		AuthenticationSuccessResponse oidcResponse = (AuthenticationSuccessResponse) authResponse;

		IAuthenticationResult result = getAuthResultByAuthCode(httpRequest, oidcResponse.getAuthorizationCode(),
				currentUri);
		System.out.println("AuthorizationCode => " + oidcResponse.getAuthorizationCode());
		System.out.println("accessToken => " + result.accessToken());

	}
}
