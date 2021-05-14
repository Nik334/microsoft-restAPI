package com.ms.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MsDate2 {

	private String dateTime;
	private String timeZone;
	
	public MsDate2(String dateTime) {
		this.setDateTime(dateTime);
		this.setTimeZone("Pacific Standard Time");
	}
}
