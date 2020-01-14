package com.bridgelabz.fundoonotes.service;

import java.util.List;

import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDto;
import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Note;

public interface NoteService {



	boolean deleteOneNote(long id, String token);

	boolean color(String color, String token, long noteId);

	int archive(String token, long noteId);

	int pinned(String token, long noteId);

	int delete(String token, long noteId);

	boolean updateNote(NoteDto noteDto, String token, long noteId);

	boolean reminder(ReminderDto reminderDto, String token, long noteId);

	List<Note> getAllNotes(String token);

	List<Label> getAllLabelsOfOneNote(String token, long noteId);

	List<Note> getAllNotesInSort(String token, String sortBy, String order);

	boolean createNote(NoteDto noteDto, String token);

	List<Note> searchByTitle(String title);

}
