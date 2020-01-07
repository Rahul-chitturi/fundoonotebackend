package com.bridgelabz.fundoonotes.servceImplementaion;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDto;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.NoteService;
import com.bridgelabz.fundoonotes.utility.JwtGenerator;

@Service
public class NoteServiceImplementation implements NoteService {

	private final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImplementation.class);

	@Autowired
	private JwtGenerator tokenGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Override
	public boolean computeSave(NoteDto noteDto, String token) {

		try {
			long id = tokenGenerator.parseJWT(token);
			LOGGER.info("Id is :" + id + " ,Description :" + noteDto.getNoteContant());
			User user = userRepository.findoneById(id);
			if (user != null) {
				NoteModel note = new NoteModel(noteDto.getNoteTitle(), noteDto.getNoteContant());
				note.setUserNote(user);
				note.setCreatedAt();
				noteRepository.insertData(note.getContant(), note.getCreatedAt(), note.getTitle(), note.getUpdatedAt(),
						note.getUserNote().getId());
				return true;
			}
			return false;

		} catch (JWTVerificationException | IllegalArgumentException | UnsupportedEncodingException e) {

			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}



	@Override
	public boolean color(String color, String token, long noteId) {
		try {
			long userId = tokenGenerator.parseJWT(token);
			LOGGER.info("Id is :" + userId +" note ID is: "+ noteId + " note ID is: "+ color);
			noteRepository.updateColor(color, userId, noteId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public int archive(String token, long noteId) {

		try {
			long userId = tokenGenerator.parseJWT(token);
			LOGGER.info("Id is :" + userId +"note ID is: "+ noteId);
			NoteModel note = noteRepository.checkById(noteId);
			LOGGER.info("name : "+ note.getId());
			if (note.isArchived()) {
				noteRepository.setArchive(false, userId, noteId);
				return 1;
			} else if(!note.isArchived()){
				noteRepository.setPinned(false, userId, noteId);
				noteRepository.setArchive(true, userId, noteId);
				return 0;
			}else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	@Override
	public int pinned(String token, long noteId) {
		try {
			long userId = tokenGenerator.parseJWT(token);
			LOGGER.info("Id is :" + userId +"note ID is: "+ noteId);
			NoteModel note = noteRepository.checkById(noteId);
			if (note.isPinned()) {
				noteRepository.setPinned(false, userId, noteId);
				return 1;
			} else if(!note.isPinned()){
				noteRepository.setArchive(false, userId, noteId);
				noteRepository.setPinned(true, userId, noteId);
				return 0;
			}else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}
	
	@Override
	public boolean deleteOneNote(long noteId, String token) {
		try {
			long userId = tokenGenerator.parseJWT(token);
			LOGGER.info("Id is :" + userId);
			NoteModel note = noteRepository.checkById(noteId);
			if (note.isDeleted()) {
				noteRepository.deleteForever(userId, noteId);
				return true;
			} 
	    	return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int delete(String token, long noteId) {
	try {
		long userId = tokenGenerator.parseJWT(token);
		LOGGER.info("Id is :" + userId +"note ID is: "+ noteId);
		NoteModel note = noteRepository.checkById(noteId);
		if (note.isDeleted()) {
			noteRepository.setPinned(false, userId, noteId);
			noteRepository.setDelete(false, userId, noteId);
			return 1;
		} else if(!note.isDeleted()){
			noteRepository.setPinned(false, userId, noteId);
			noteRepository.setDelete(true, userId, noteId);
			return 0;
		}else {
			return -1;
		}
	}catch (Exception e) {
e.printStackTrace();
		return -1;
	}
	}



	@Override
	public boolean updateNote(NoteDto noteDto, String token , long noteId) {
		
		try {
			long id = tokenGenerator.parseJWT(token);
			LOGGER.info("Id is :" + id + " ,Description :" + noteDto.getNoteContant());
			User user = userRepository.findoneById(id);
			if (user != null) {
				NoteModel note = noteRepository.checkById(noteId);
				note.setContant(noteDto.getNoteContant());
				note.setTitle(noteDto.getNoteTitle());
				note.setUpdatedAt();
				noteRepository.updateData(note.getContant(), note.getTitle(), note.getUpdatedAt() , id ,  noteId);
				return true;
			}
			return false;

		
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		

	}



	@Override
	public boolean reminder(ReminderDto reminderDto, String token, long noteId) {

		try {
			long id = tokenGenerator.parseJWT(token);
			LOGGER.info("Id is :" + id );
			User user = userRepository.findoneById(id);
			if (user != null) {
				NoteModel note = noteRepository.checkById(noteId);
				note.setLocalReminderStatus(reminderDto.getLocalReminderStatus());
				note.setLocalReminder(reminderDto.getLocalReminder());
				System.out.println(note.getLocalReminder());
				note.setUpdatedAt();
				noteRepository.reminder(note.getLocalReminderStatus(), note.getLocalReminder(), note.getUpdatedAt() , id ,  noteId);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
