package com.bridgelabz.fundoonotes.servceImplementaion;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.CollaboratorDto;
import com.bridgelabz.fundoonotes.exception.CustomException;
import com.bridgelabz.fundoonotes.model.Collaborator;
import com.bridgelabz.fundoonotes.model.Note;
import com.bridgelabz.fundoonotes.repository.CollaboratorRepository;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.service.CollaboratorService;
import com.bridgelabz.fundoonotes.utility.JwtGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CollaboratorImplementation implements CollaboratorService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private JwtGenerator tokenGenerator;

	@Autowired
	private CollaboratorRepository collaboratorRepository;

	@Autowired
	private RedisTemplate<String, Object> redis;

	@Override
	public Collaborator addCollaborator(CollaboratorDto collaboratorDto, String token, long noteId) {

		Long userId = getRedisCecheId(token);

		Note noteModel = noteRepository.FindByNotedIdAndUserId(noteId, userId);
		Collaborator toFindAllreadyExists = collaboratorRepository.findOneByEmail(collaboratorDto.getEmail());
		if (noteModel != null && toFindAllreadyExists == null) {
			Collaborator collaborator = new Collaborator();
			BeanUtils.copyProperties(collaboratorDto, collaborator);
			collaborator.setNote(noteModel);
			collaboratorRepository.save(collaborator);
			return collaborator;
		} else {
			throw new CustomException("User Not Found or already Exist");
		}

	}

	private Long getRedisCecheId(String token) {
		String[] splitedToken = token.split("\\.");
		String redisTokenKey = splitedToken[1] + splitedToken[2];
		if (redis.opsForValue().get(redisTokenKey) == null) {
			Long idForRedis = tokenGenerator.parseJWT(token);
			log.info("idForRedis is :" + idForRedis);
			redis.opsForValue().set(redisTokenKey, idForRedis, 2 * 60, TimeUnit.SECONDS);
		}
		Long userId = (Long) redis.opsForValue().get(redisTokenKey);
		return userId;
	}

	@Override
	public int deleteCollaborator(Long cId, String token, Long noteId) {
		int i = 0;
		Long userId = getRedisCecheId(token);
		Note noteModel = noteRepository.FindByNotedIdAndUserId(noteId, userId);
		if (noteModel != null) {
			i = collaboratorRepository.deleteOneById(cId);
			
		}else {
			return i;
		}
		return i;
	}

}
