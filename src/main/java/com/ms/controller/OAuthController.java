//package com.ms.controller;
//
//import java.net.MalformedURLException;
//import java.util.List;
//import java.util.UUID;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.azure.identity.AuthorizationCodeCredential;
//import com.azure.identity.AuthorizationCodeCredentialBuilder;
//import com.google.common.collect.ImmutableList;
//import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
//import com.microsoft.graph.models.User;
//import com.microsoft.graph.requests.GraphServiceClient;
//
//@RestController
//public class OAuthController {
//
//	@Autowired
//	private AuthHelper authHelper;
//
//	@GetMapping("/oauth")
//	public String getOAuthUrl(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
//			throws MalformedURLException {
//		String state = UUID.randomUUID().toString();
//		String nonce = UUID.randomUUID().toString();
//
//		String authorizationCodeUrl = authHelper.getAuthorizationCodeUrl(httpRequest.getParameter("claims"),
//				"User.Read", authHelper.getRedirectUriGraph(), state, nonce);
//		return authorizationCodeUrl;
//	}
//
////	@GetMapping("/myapp")
//	public User getCode(@RequestParam("code") String code) throws Throwable {
//
//		final AuthorizationCodeCredential authCodeCredential = new AuthorizationCodeCredentialBuilder()
//				.clientId(authHelper.getClientId()).clientSecret(authHelper.getClientSecret()) // required for web apps,
//																								// do not set for native
//																								// apps
//				.authorizationCode(code).redirectUrl(authHelper.getRedirectUriGraph()).build();
//
////		Set<String> scope = ImmutableSet.of("User.Read");
//		List<String> of = ImmutableList.of("User.Read");
//		final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(of,
//				authCodeCredential);
//
//		final GraphServiceClient graphClient = GraphServiceClient.builder()
//				.authenticationProvider(tokenCredentialAuthProvider).buildClient();
//
//		final User me = graphClient.me().buildRequest().get();
////		IClientCredential credential = ClientCredentialFactory.createFromSecret(authHelper.getClientSecret());
////
////		ConfidentialClientApplication cca = ConfidentialClientApplication.builder(authHelper.getClientId(), credential)
////				.authority(authHelper.getAuthority()).build();
////
////		Set<String> scope = ImmutableSet.of("User.Read");
////
////		ClientCredentialParameters parameters = ClientCredentialParameters.builder(scope).build();
////
////		IAuthenticationResult result = cca.acquireToken(parameters).join();
////		final InteractiveBrowserCredential interactiveBrowserCredential = new InteractiveBrowserCredentialBuilder()
////				.clientId(authHelper.getClientId()).redirectUrl(authHelper.getRedirectUriGraph()).build();
////
////		final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(
////				Arrays.asList("User.Read"), interactiveBrowserCredential);
////		
//////		System.out.println("tokenCredentialAuthProvider => " + tokenCredentialAuthProvider);
////
////		final GraphServiceClient graphClient = GraphServiceClient.builder()
////				.authenticationProvider(tokenCredentialAuthProvider).buildClient();
////
////		final User me = graphClient.me().buildRequest().get();
//
//		return me;
//	}
//
//}
