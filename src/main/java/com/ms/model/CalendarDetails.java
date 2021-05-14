package com.ms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarDetails {

	private List<MsAttendee> attendees = new ArrayList<>();
	private Date createdDateTime;
	private MsOrganizer organizer;
	private Integer reminderMinutesBeforeStart;
	private Boolean isReminderOn;
	private Boolean isOnlineMeeting;
	private Boolean isOrganizer;
	private String bodyPreview;
	private String subject;
	private MsDate end;
	private String onlineMeetingUrl;
	private String webLink;
	private String onlineMeetingProvider;
	private MsDate start;
	private MsStatus responseStatus;
	private Date lastModifiedDateTime;
	private MsOnlineMeeting onlineMeeting;
}
