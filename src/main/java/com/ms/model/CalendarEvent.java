package com.ms.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarEvent {

	private List<CalendarDetails> value = new ArrayList<>();
}
