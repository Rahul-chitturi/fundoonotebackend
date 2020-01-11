package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.CollaboratorDto;
import com.bridgelabz.fundoonotes.model.Collaborator;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.CollaboratorService;

@RestController
@RequestMapping("/collaborate")
public class CollaboratorController {
	
	@Autowired
	private CollaboratorService collaboratorService;


	@PostMapping("/addcollaborator/{id}")
	public ResponseEntity<Response> addCollaborator(@Valid  @RequestBody CollaboratorDto collaboratorDto,@PathVariable("id") long noteId ,@RequestHeader("token") String token  , BindingResult bindingResult) {
   
		if(bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(bindingResult.getAllErrors().get(0).getDefaultMessage(), 400));
		}
		
		Collaborator collaborator = collaboratorService.addCollaborator(collaboratorDto , token , noteId);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new Response("collaborated successfully", 200, collaborator));
	}
	
	@DeleteMapping("/deletecollaborator/{id}")
	public ResponseEntity<Response> deleteCollaborator(@PathVariable(value = "id") Long noteId ,@RequestHeader("token") String token , @RequestHeader("collaboratorId") Long cId ) {
 int i =    collaboratorService.deleteCollaborator(cId , token , noteId);
		return  i==1? ResponseEntity.status(HttpStatus.CREATED)
				.body(new Response("deleted successfully", 200, null)):ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
	}
	
	@GetMapping("/getallnotecollaborats/{id}")
	public ResponseEntity<Response> getAllCollaborator(@PathVariable(value = "id") Long noteId ,@RequestHeader("token") String token  ) {
 List<Collaborator> collList =   collaboratorService.getAllNoteCollaborators(token , noteId);
		return collList!= null? ResponseEntity.status(HttpStatus.CREATED)
				.body(new Response("all note collaborators are", 200, collList)):ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
	}
	
}
