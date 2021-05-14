package com.ms.model;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MsDate {

	private Date dateTime;
	private String timeZone;
	
	public MsDate(Date dateTime) {
		this.setDateTime(dateTime);
		this.setTimeZone("Pacific Standard Time");
	}
}
