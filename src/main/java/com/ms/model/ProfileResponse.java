package com.ms.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {

	private String displayName;
	private String surname;
	private String givenName;
	private String id;
	private String userPrincipalName;
}
