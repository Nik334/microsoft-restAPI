package com.ms.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventPostObject {

	private String subject;
	private MsDate2 start;
	private MsDate2 end;
	private List<MsAttendee> attendees = new ArrayList<>();
	private Boolean allowNewTimeProposals = true;
	private Boolean isOnlineMeeting = true;
	private String onlineMeetingProvider;

	public void toEntry(String subject, String onlineMeetingProvider) {
		this.setSubject(subject);
		this.setOnlineMeetingProvider(onlineMeetingProvider);
	}
}
