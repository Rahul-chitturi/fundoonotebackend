package com.bridgelabz.fundoonotes.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.CollaboratorDto;
import com.bridgelabz.fundoonotes.response.Response;

@RestController
@RequestMapping("/collaborate")
public class CollaboratorController {


	@PostMapping("/addcollaborator/{id}")
	public ResponseEntity<Response> addCollaborator(@Valid  @RequestBody CollaboratorDto collaboratorDto,@PathVariable("id") long noteId ,@RequestHeader("token") String token  , BindingResult bindingResult){
   
		if(bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(bindingResult.getAllErrors().get(0).getDefaultMessage(), 400));
		}
		return null;
	
		
		
		
	}
}