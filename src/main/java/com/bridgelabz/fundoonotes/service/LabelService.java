package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.model.Label;


public interface LabelService {

	Label createlabel(LabelDto labelDto, String token);

	Label createOrMapWithNote(LabelDto labelDto, Long noteId, String token) ;

	boolean removeLabels(String token, Long noteId, Long labelId);

	boolean deletepermanently(String token, Long labelId) ;

	boolean updateLabel(String token, Long labelId, LabelDto labelDto);



	boolean addLabels(String token, long noteId, long labelId);

}