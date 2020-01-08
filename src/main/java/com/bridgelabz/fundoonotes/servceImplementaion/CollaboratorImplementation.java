package com.bridgelabz.fundoonotes.servceImplementaion;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.CollaboratorDto;
import com.bridgelabz.fundoonotes.model.Collaborator;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.repository.CollaboratorRepository;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.service.CollaboratorService;
import com.bridgelabz.fundoonotes.utility.JwtGenerator;
@Service
public class CollaboratorImplementation implements CollaboratorService {

	@Autowired
	private NoteRepository noteRepository;
	
	@Autowired
	private JwtGenerator tokenGenerator;
	
	@Autowired
	private CollaboratorRepository collaboratorRepository;

	@Override
	public Collaborator addCollaborator( CollaboratorDto collaboratorDto, String token, long noteId) {
        
		try {
		 long userId  = tokenGenerator.parseJWT(token);
	
		 NoteModel noteModel = noteRepository.FindByNotedIdAndUserId(noteId, userId);
		
		 if(noteModel !=null) {
			 Collaborator collaborator =  new Collaborator();
			 BeanUtils.copyProperties(collaboratorDto, collaborator);
	
		 }
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

	
	
}
