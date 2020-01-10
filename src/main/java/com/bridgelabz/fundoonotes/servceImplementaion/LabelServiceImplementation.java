package com.bridgelabz.fundoonotes.servceImplementaion;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.exception.LabelAlreadyExistException;
import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Note;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.repository.LabelRepository;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.LabelService;
import com.bridgelabz.fundoonotes.utility.JwtGenerator;

@Service
public  class LabelServiceImplementation implements LabelService {

	@Autowired
	private JwtGenerator jwtGenerator;

	@Autowired
	private UserRepository userRepository;


	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private NoteRepository noterepository;

	@Override
	public Label createlabel(LabelDto labelDto, String token)  {
		long userId = jwtGenerator.parseJWT(token);
		User isUserAvailable = userRepository.findoneById(userId);
		if (isUserAvailable!=null) {
			String labelName = labelDto.getName();
			Label label  = labelRepository.findOneByName(labelName);
			if (label == null) {
			Label newLabel = new Label();
			BeanUtils.copyProperties(labelDto, newLabel);
				newLabel.setUserLabel(isUserAvailable);
				labelRepository.insertLabelData(newLabel.getName(), userId);
				return newLabel;
			} else {
				throw new LabelAlreadyExistException("Label is already exist...");
			}
		}
		return null;

	}

	@Override
	public Label createOrMapWithNote(LabelDto labelDto, Long noteId, String token)  {
		
		Long userId = jwtGenerator.parseJWT(token);
		User isUserAvailable = userRepository.findoneById(userId);
		if (isUserAvailable!=null) {
			String labelName = labelDto.getName();
			Label label  = labelRepository.findOneByName(labelName);
			if (label == null) {
			Label newLabel = new Label();
			BeanUtils.copyProperties(labelDto, newLabel);
				newLabel.setUserLabel(isUserAvailable);
				labelRepository.save(newLabel);
			   Note noteInfo = noterepository.checkById(noteId);
				if (noteInfo!=null) {
                      noterepository.insertDataToMap(noteId , newLabel.getLableId());
					return  newLabel;
				}
				return newLabel;
			} else {
				throw new LabelAlreadyExistException("Label is already exist...");
			}
		}
		return null;
		
	
	}

	@Override
	public boolean removeLabels(String token, Long noteId, Long labelId) {
		
		return false;
	}

	@Override
	public boolean deletepermanently(String token, Long labelId) {
		
		return false;
	}

	@Override
	public boolean updateLabel(String token, Long labelId, LabelDto labelDto) {

		return false;
	}

	@Override
	public boolean addLabels(String token, long noteId, long labelId) {

		return false;
	}

	

}