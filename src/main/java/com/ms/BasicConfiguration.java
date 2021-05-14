package com.ms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("aad")
public class BasicConfiguration {

	private String clientId;
    private String authority;
    private String redirectUriGraph;
    private String secretKey;
    private String msGraphEndpointHost;

    public String getAuthority(){
        if (!authority.endsWith("/")) {
            authority += "/";
        }
        return authority;
    }
}
