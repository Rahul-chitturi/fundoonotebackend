package com.bridgelabz.fundoonotes.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class ReminderDto {

	@Getter
	@Setter
	private String localReminderStatus;

	@Getter
	private Date localReminder;

	public void setLocalReminder(String localReminder) {
		try {
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.localReminder = date.parse(localReminder);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
