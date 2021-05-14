package com.ms.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MsAttendee {

	private String type;
	private MsStatus status;
	private MsEmailAddress emailAddress;

	public MsAttendee(String address, String name, String type) {
		MsEmailAddress msEmailAddress = new MsEmailAddress();
		msEmailAddress.setAddress(address);
		msEmailAddress.setName(name);
		this.setEmailAddress(msEmailAddress);
		this.setType(type);
	}

}
