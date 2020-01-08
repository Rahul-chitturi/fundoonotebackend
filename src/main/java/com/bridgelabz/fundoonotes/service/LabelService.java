package com.bridgelabz.fundoonotes.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.NoteModel;


public interface LabelService {

	boolean createlabel(LabelDto labelDto, String token) throws Exception;

	boolean createOrMapWithNote(LabelDto labelDto, long noteId, String token) throws Exception;

	boolean removeLabels(String token, long noteId, long labelId);

	boolean deletepermanently(String token, long labelId) throws Exception;

	boolean updateLabel(String token, long labelId, LabelDto labelDto)throws Exception;

	List<Label> getAllLabels(String token) throws Exception;

	List<NoteModel> getAllNotes(String token, long labelId) throws Exception;

	boolean addLabels(String token, long noteId, long labelId);

}