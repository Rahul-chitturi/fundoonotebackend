package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.LabelDto;


public interface LabelService {

	boolean createlabel(LabelDto labelDto, String token) throws Exception;

	boolean createOrMapWithNote(LabelDto labelDto, long noteId, String token) throws Exception;

	boolean removeLabels(String token, long noteId, long labelId);

	boolean deletepermanently(String token, long labelId) throws Exception;

	boolean updateLabel(String token, long labelId, LabelDto labelDto)throws Exception;



	boolean addLabels(String token, long noteId, long labelId);

}