package com.bridgelabz.fundoonotes.service;

import java.util.List;

import com.bridgelabz.fundoonotes.model.Note;

public interface ElasticSearchService {

	String createNote(Note note);

	void updateNote(Long noteId);

	String deleteNote(Long noteId);

	List<Note> searchbytitle(String title);

	

	
	
}
