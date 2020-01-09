package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.CollaboratorDto;
import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.model.Collaborator;

public interface CollaboratorService {

	Collaborator addCollaborator(CollaboratorDto collaboratorDto, String token, long noteId) ;

}
