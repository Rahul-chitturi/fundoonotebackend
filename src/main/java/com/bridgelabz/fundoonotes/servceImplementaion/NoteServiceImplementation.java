package com.bridgelabz.fundoonotes.servceImplementaion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDto;
import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Note;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.repository.LabelRepository;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.ElasticSearchService;
import com.bridgelabz.fundoonotes.service.NoteService;
import com.bridgelabz.fundoonotes.utility.JwtGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoteServiceImplementation implements NoteService {


	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private RedisTemplate<String, Object> redis;

	@Autowired
	private ElasticSearchService elasticService;
	
	@Autowired
	private LabelRepository labelRepository; 

	@Override
	public boolean createNote(NoteDto noteDto, String token) {
		try {
			Long userId = getRedisCecheId(token);
			log.info("Id is :" + userId + " ,Description :" + noteDto.getNoteContant());
			User user = userRepository.findoneById(userId);
			if (user != null) {
				Note note = new Note(noteDto.getNoteTitle(), noteDto.getNoteContant());
				note.setUserNote(user);
				note.setCreatedAt();
				note.setNoteColor("ffffff");
				Note result = noteRepository.save(note);// insertData(note.getContant(), note.getCreatedAt(),
														// note.getTitle(), note.getUpdatedAt(),
				// note.getUserNote().getId());
				if (result != null) {
					// note = noteRepository.findbytitle(note.getTitle());
					log.info(elasticService.createNote(result));
					return true;
				}
				return false;
			}
			return false;

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean color(String color, String token, long noteId) {
		try {
			Long userId = getRedisCecheId(token);
			log.info("Id is :" + userId + "note ID is: " + noteId);
			noteRepository.updateColor(color, userId, noteId);
			elasticService.updateNote(noteId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public int archive(String token, long noteId) {

		try {
			Long userId = getRedisCecheId(token);
			Note note = noteRepository.checkById(noteId);
		
			if (note.isArchived()) {
				noteRepository.setArchive(false, userId, noteId);
				elasticService.updateNote(noteId);
				return 1;
			} else if (!note.isArchived()) {
				noteRepository.setPinned(false, userId, noteId);
				noteRepository.setArchive(true, userId, noteId);
				elasticService.updateNote(noteId);
				return 0;
			} else {
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
			Long userId = getRedisCecheId(token);
			log.info("Id is :" + userId + "note ID is: " + noteId);
			Note note = noteRepository.checkById(noteId);
			if (note.isPinned()) {
				noteRepository.setPinned(false, userId, noteId);
				elasticService.updateNote(noteId);
				return 1;
			} else if (!note.isPinned()) {
				noteRepository.setArchive(false, userId, noteId);
				noteRepository.setPinned(true, userId, noteId);
				elasticService.updateNote(noteId);
				return 0;
			} else {
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
			Long userId = getRedisCecheId(token);
			log.info("Id is :" + userId + "note ID is: " + noteId);
			Note note = noteRepository.checkById(noteId);
			if (note != null && note.isDeleted()) {
				labelRepository.removeNoteFromLabel( noteId);
				elasticService.deleteNote(noteId);
				noteRepository.deleteForever(userId, noteId);
				return true;
			}
			return false;
		} catch (Exception e) {
			
			return false;
		}
	}

	@Override
	public int delete(String token, long noteId) {
		try {
			Long userId = getRedisCecheId(token);
			log.info("Id is :" + userId + "note ID is: " + noteId);
			Note note = noteRepository.checkById(noteId);
			if (note.isDeleted()) {
				noteRepository.setPinned(false, userId, noteId);
				noteRepository.setDelete(false, userId, noteId);
				elasticService.updateNote(noteId);
				return 1;
			} else if (!note.isDeleted()) {
				noteRepository.setPinned(false, userId, noteId);
				noteRepository.setDelete(true, userId, noteId);
				elasticService.updateNote(noteId);
				return 0;
			} else {
				return -1;
			}
		} catch (Exception e) {

			return -1;
		}
	}

	@Override
	public boolean updateNote(NoteDto noteDto, String token, long noteId) {

		try {
			Long id = getRedisCecheId(token);
			log.info("Id is :" + id + "note ID is: " + noteId);
			User user = userRepository.findoneById(id);
			if (user != null) {
				Note note = noteRepository.checkById(noteId);
				note.setContant(noteDto.getNoteContant());
				note.setTitle(noteDto.getNoteTitle());
				note.setUpdatedAt();
				noteRepository.updateData(note.getContant(), note.getTitle(), note.getUpdatedAt(), id, noteId);
				elasticService.updateNote(noteId);
				return true;
			}
			return false;

		} catch (Exception e) {
		
			return false;
		}

	}

	@Override
	public boolean reminder(ReminderDto reminderDto, String token, long noteId) {

		try {
			Long id = getRedisCecheId(token);
			log.info("Id is :" + id + "note ID is: " + noteId);
			User user = userRepository.findoneById(id);
			if (user != null) {
				Note note = noteRepository.checkById(noteId);
				note.setLocalReminderStatus(reminderDto.getLocalReminderStatus());
				note.setLocalReminder(reminderDto.getLocalReminder());

				note.setUpdatedAt();
				noteRepository.reminder(note.getLocalReminderStatus(), note.getLocalReminder(), note.getUpdatedAt(), id,
						noteId);
				elasticService.updateNote(noteId);
				return true;
			}
			return false;
		} catch (Exception e) {

			return false;
		}
	}

	private Long getRedisCecheId(String token) {
		String[] splitedToken = token.split("\\.");
		String redisTokenKey = splitedToken[1] + splitedToken[2];
		if (redis.opsForValue().get(redisTokenKey) == null) {
			Long idForRedis =JwtGenerator.decodeJWT(token);
			log.info("idForRedis is :" + idForRedis);
			redis.opsForValue().set(redisTokenKey, idForRedis, 3 * 60, TimeUnit.SECONDS);
		}
		Long userId =  (Long) redis.opsForValue().get(redisTokenKey);
		return userId;
	}

	@Override
	public List<Note> getAllNotes(String token) {
		Long userId = getRedisCecheId(token);
		User isUserAvailable = userRepository.findoneById(userId);
		if (isUserAvailable != null) {
                    
			return  noteRepository.findAll(userId);
		}
		return  null ;
	}

	@Override
	public List<Label> getAllLabelsOfOneNote(String token, long noteId) {
		Long userId = getRedisCecheId(token);
		User isUserAvailable = userRepository.findoneById(userId);
		if (isUserAvailable != null) {
			Note note = noteRepository.checkById(noteId);
			if (note != null) {
				return	 noteRepository.getLabelByNoteId(noteId);
			
			}
		}
		return null;
	}

	@Override
	public List<Note> getAllNotesInSort(String token, String sortBy, String order) {
		if (sortBy.equals("name") && order.equals("asc")) {
			return getAllNotes(token);
		}
		Long userId = getRedisCecheId(token);
		User isUserAvailable = userRepository.findoneById(userId);
		if (isUserAvailable != null) {
			if (sortBy.equals("name") && order.equals("desc")) {
				List<Note> notes = noteRepository.findAllInDesc(userId);
				return notes;
			} else if (sortBy.equals("date") && order.equals("desc")) {
				List<Note> notes = noteRepository.findAllInDescbyDate(userId);
				return notes;
			} else if (sortBy.equals("date") && order.equals("asc")) {
				List<Note> notes = noteRepository.findAllInbyDate(userId);
				return notes;
			} else {
				return getAllNotes(token);
			}
		}
		return null;
	}
	
	@Override
	public List<Note> searchByTitle(String title) {
		List<Note> notes=elasticService.searchbytitle(title);
		if(notes!=null) {
		return notes;
		}
		else {
			return null;
		}
	}

}
