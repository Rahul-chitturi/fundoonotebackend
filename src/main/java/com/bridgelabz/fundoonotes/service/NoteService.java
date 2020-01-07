package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDto;

public interface NoteService {

	boolean computeSave(NoteDto noteDto, String token);

	boolean deleteOneNote(long id, String token);

	boolean color(String color, String token, long noteId);

	int archive(String token, long noteId);

	int pinned(String token, long noteId);

	int delete(String token, long noteId);

	boolean updateNote(NoteDto noteDto, String token, long noteId);

	boolean reminder(ReminderDto reminderDto, String token, long noteId);

}
