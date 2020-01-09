package com.bridgelabz.fundoonotes.servceImplementaion;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDto;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.NoteService;
import com.bridgelabz.fundoonotes.utility.JwtGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoteServiceImplementation implements NoteService {

	private final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImplementation.class);

	@Autowired
	private JwtGenerator tokenGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private RedisTemplate<String, Object> redis;

	@Override
	public boolean computeSave(NoteDto noteDto, String token) {
		try {
			Long userId = getRedisCecheId(token);
			log.info("Id is :" + userId + " ,Description :" + noteDto.getNoteContant());
			User user = userRepository.findoneById(userId);
			if (user != null) {
				NoteModel note = new NoteModel(noteDto.getNoteTitle(), noteDto.getNoteContant());
				note.setUserNote(user);
				note.setCreatedAt();
				noteRepository.insertData(note.getContant(), note.getCreatedAt(), note.getTitle(), note.getUpdatedAt(),
						note.getUserNote().getId());
				return true;
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
			NoteModel note = noteRepository.checkById(noteId);
			LOGGER.info("name : " + note.getId());
			if (note.isArchived()) {
				noteRepository.setArchive(false, userId, noteId);
				return 1;
			} else if (!note.isArchived()) {
				noteRepository.setPinned(false, userId, noteId);
				noteRepository.setArchive(true, userId, noteId);
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
			NoteModel note = noteRepository.checkById(noteId);
			if (note.isPinned()) {
				noteRepository.setPinned(false, userId, noteId);
				return 1;
			} else if (!note.isPinned()) {
				noteRepository.setArchive(false, userId, noteId);
				noteRepository.setPinned(true, userId, noteId);
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
			NoteModel note = noteRepository.checkById(noteId);
			if (note.isDeleted()) {
				noteRepository.deleteForever(userId, noteId);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int delete(String token, long noteId) {
		try {
			Long userId = getRedisCecheId(token);
			log.info("Id is :" + userId + "note ID is: " + noteId);
			NoteModel note = noteRepository.checkById(noteId);
			if (note.isDeleted()) {
				noteRepository.setPinned(false, userId, noteId);
				noteRepository.setDelete(false, userId, noteId);
				return 1;
			} else if (!note.isDeleted()) {
				noteRepository.setPinned(false, userId, noteId);
				noteRepository.setDelete(true, userId, noteId);
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
	public boolean updateNote(NoteDto noteDto, String token, long noteId) {

		try {
			Long id = getRedisCecheId(token);
			log.info("Id is :" + id + "note ID is: " + noteId);
			User user = userRepository.findoneById(id);
			if (user != null) {
				NoteModel note = noteRepository.checkById(noteId);
				note.setContant(noteDto.getNoteContant());
				note.setTitle(noteDto.getNoteTitle());
				note.setUpdatedAt();
				noteRepository.updateData(note.getContant(), note.getTitle(), note.getUpdatedAt(), id, noteId);
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
			Long id = getRedisCecheId(token);
			log.info("Id is :" + id + "note ID is: " + noteId);
			User user = userRepository.findoneById(id);
			if (user != null) {
				NoteModel note = noteRepository.checkById(noteId);
				note.setLocalReminderStatus(reminderDto.getLocalReminderStatus());
				note.setLocalReminder(reminderDto.getLocalReminder());
				System.out.println(note.getLocalReminder());
				note.setUpdatedAt();
				noteRepository.reminder(note.getLocalReminderStatus(), note.getLocalReminder(), note.getUpdatedAt(), id,
						noteId);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private Long getRedisCecheId(String token) {
		String[] splitedToken = token.split("\\.");
		String redisTokenKey = splitedToken[1] + splitedToken[2];
		if (redis.opsForValue().get(redisTokenKey) == null) {
			Long idForRedis = tokenGenerator.parseJWT(token);
			log.info("idForRedis is :" + idForRedis);
			redis.opsForValue().set(redisTokenKey, idForRedis, 3 * 60, TimeUnit.SECONDS);
		}
		Long userId = (Long) redis.opsForValue().get(redisTokenKey);
		return userId;
	}

}
