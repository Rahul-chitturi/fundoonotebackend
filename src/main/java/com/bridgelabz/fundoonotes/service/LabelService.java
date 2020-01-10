package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.model.Label;

public interface LabelService {

	Label createlabel(LabelDto labelDto, String token);

	Label createOrMapWithNote(LabelDto labelDto, Long noteId, String token);

	Label removeLabels(String token, Long noteId, Long labelId);

	Label deletepermanently(String token, Long labelId);

	Label updateLabel(String token, Long labelId, LabelDto labelDto);

	Label addLabels(String token, long noteId, long labelId);

}