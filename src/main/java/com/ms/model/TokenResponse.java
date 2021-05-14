package com.ms.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {

	private String token_type;
	
	private String scope;
	
	private Integer expires_in;
	
	private Integer ext_expires_in;
	
	private String access_token;
	
	private String refresh_token;
	
	private String id_token;
}
