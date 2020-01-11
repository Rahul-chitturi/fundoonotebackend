package com.bridgelabz.fundoonotes.servceImplementaion;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LabelServiceImplementation implements LabelService {

	@Autowired
	private JwtGenerator jwtGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private NoteRepository noterepository;

	@Autowired
	private RedisTemplate<String, Object> redis;

	@Override
	public Label createlabel(LabelDto labelDto, String token) {
		long userId = jwtGenerator.parseJWT(token);
		User isUserAvailable = userRepository.findoneById(userId);
		if (isUserAvailable != null) {
			String labelName = labelDto.getName();
			Label label = labelRepository.findOneByName(labelName);
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
	public Label createOrMapWithNote(LabelDto labelDto, Long noteId, String token) {

		Long userId = jwtGenerator.parseJWT(token);
		User isUserAvailable = userRepository.findoneById(userId);
		Note noteInfo = noterepository.checkById(noteId);
		if (isUserAvailable != null) {
			if (noteInfo != null) {
				String labelName = labelDto.getName();
				Label label = labelRepository.findOneByName(labelName);
				// Label labelNote =
				// labelRepository.findoneByLabelIdAndNoteId(label.getLableId(), noteId);
				if (label == null) {
					Label newLabel = new Label();
					BeanUtils.copyProperties(labelDto, newLabel);
					newLabel.setUserLabel(isUserAvailable);
					labelRepository.insertLabelData(newLabel.getName(), userId);
					Label labelCreate = labelRepository.findOneByName(newLabel.getName());
					noterepository.insertDataToMap(noteId, labelCreate.getLableId());
					
					return labelCreate;
				} else {
					Object map = labelRepository.findoneByLabelIdAndNoteId(label.getLableId(), noteId);
					//map.stream().forEach((result)->{System.out.println(result[0]);});
					if (map == null) {
						noterepository.insertDataToMap(noteId, label.getLableId());
					}
					return label;
				}
			}
			return null;
		} else {
			throw new LabelAlreadyExistException("Label is already exist...");
		}

	}

	@Override
	public Label removeLabels(String token, Long noteId, Long labelId) {
		Long userId = jwtGenerator.parseJWT(token);
		Note isNoteAvailable = noterepository.checkById(noteId);
		if (isNoteAvailable != null) {
			Label isLabelAvailable = labelRepository.findoneById(labelId, userId);
			if (isLabelAvailable != null) {
				labelRepository.removeInMapTable(isLabelAvailable.getLableId(), noteId);

				return isLabelAvailable;
			}
		}
		return null;

	}

	@Override
	public Label deletepermanently(String token, Long labelId) {
		Long userId = getRedisCecheId(token);
		Label isLabelAvailable = labelRepository.findoneById(labelId, userId);
		if (isLabelAvailable != null) {
			int i = labelRepository.removeInMapTablePermanently(isLabelAvailable.getLableId());
			if (i == 1) {
				labelRepository.remove(labelId, userId);
				return isLabelAvailable;
			}
			return null;
		}
		return null;
	}

	@Override
	public Label updateLabel(String token, Long labelId, LabelDto labelDto) {

		Long userId = getRedisCecheId(token);
		User isUserAvailable = userRepository.findoneById(userId);
		if (isUserAvailable != null) {
			Label isLabelAvailable = labelRepository.findoneById(labelId, userId);
			if (isLabelAvailable != null) {
				BeanUtils.copyProperties(labelDto, isLabelAvailable);
				labelRepository.update(isLabelAvailable.getName(), labelId);
				return isLabelAvailable;
			}
		}
		return null;
	}

	@Override
	public Label addLabels(String token, long noteId, long labelId) {
		Long userId = getRedisCecheId(token);
		Note isNoteAvailable = noterepository.checkById(noteId);
		if (isNoteAvailable != null) {
			Label isLabelAvailable = labelRepository.findoneById(labelId, userId);
			if (isLabelAvailable != null) {
				Object map = labelRepository.findoneByLabelIdAndNoteId(isLabelAvailable.getLableId(), noteId);
				if (map == null) {
					noterepository.insertDataToMap(noteId, isLabelAvailable.getLableId());
				}
				return isLabelAvailable;
			}
		}
		return null;
	}

	@Override
	public List<Label> getAllLabels(String token) {
		Long userId = getRedisCecheId(token);
		User isUserAvailable = userRepository.findoneById(userId);
		if (isUserAvailable != null) {
		List<Label> labels=  labelRepository.findAll(userId);
	
		labels.stream().map(e -> LabelDto.valueOf(e)).collect(Collectors.toList());
		return labels;
		}
		return null;
	}

	@Override
	public List<Note> getAllNotes(String token, long labelId) {
		Long userId = getRedisCecheId(token);
		User isUserAvailable = userRepository.findoneById(userId);
		if (isUserAvailable!=null) {
			Label isLabelAvailable = labelRepository.findoneById(labelId , userId);
			if (isLabelAvailable!=null) {
				List<Note> list =labelRepository.findListOfNoteOfLabel(labelId);
		list.stream().forEach((result)->{System.out.println(result.getId());});
				return list;
			}
		}
		return null;
	}

	private Long getRedisCecheId(String token) {
		String[] splitedToken = token.split("\\.");
		String redisTokenKey = splitedToken[1] + splitedToken[2];
		if (redis.opsForValue().get(redisTokenKey) == null) {
			Long idForRedis = jwtGenerator.parseJWT(token);
			log.info("idForRedis is :" + idForRedis);
			redis.opsForValue().set(redisTokenKey, idForRedis, 3 * 60, TimeUnit.SECONDS);
		}
		Long userId = (Long) redis.opsForValue().get(redisTokenKey);
		return userId;
	}

}