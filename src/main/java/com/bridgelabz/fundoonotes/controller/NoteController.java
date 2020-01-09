package com.bridgelabz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDto;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.NoteService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/notes")
public class NoteController {

	@Autowired
	private NoteService noteService;
	
	@Autowired
	private RedisTemplate<String, Object> redis;

	/*
	 * API to create notes
	 */
	@PostMapping("/create")
	@ApiOperation(value = "Api to Create Note for User in fundoonotes" , response = Response.class)
	private ResponseEntity<Response> createNote(@RequestBody NoteDto noteDto, @RequestHeader("token") String token)
		 {


		boolean result = noteService.computeSave(noteDto, token);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is created successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	@PutMapping("/color/{id}")
	@ApiOperation(value = "Api to Create Note for User In fundoonotes" , response = Response.class)
	private ResponseEntity<Response> color(@PathVariable("id") long noteId, @RequestParam String color,
			@RequestHeader("token") String token) {

		boolean result = noteService.color(color, token, noteId);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Color changed succussfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}

	}

	@PutMapping("/archive/{id}")
	@ApiOperation(value = "Api to Archive Note for fundoonotes" , response = Response.class)
	private ResponseEntity<Response> archive(@PathVariable("id") long noteId, @RequestHeader("token") String token) {

		int result = noteService.archive(token, noteId);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully removed from Archive", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully Archived", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	@PutMapping("/pinned/{id}")
	@ApiOperation(value = "Api to pin and unpin Note for fundoonotes" , response = Response.class)
	private ResponseEntity<Response> pinned(@PathVariable("id") long noteId, @RequestHeader("token") String token) {
		int result = noteService.pinned(token, noteId);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully unPinned", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully Pinned", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	@PutMapping(value = {"/trash/{id}","/restore/{id}"})
	@ApiOperation(value = "Api to delete  Note for fundoonotes" , response = Response.class)
	private ResponseEntity<Response> trash(@PathVariable("id") long noteId, @RequestHeader("token") String token) {
		int result = noteService.delete(token, noteId);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully restored", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully moved to trash ", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	@DeleteMapping("/deleteforever/{id}")
	@ApiOperation(value = "Api to deleteNote forever Note for fundoonotes" , response = Response.class)
	private ResponseEntity<Response> deleteOneNote(@PathVariable("id") long id, @RequestHeader("token") String token)
			throws Exception {

		boolean result = noteService.deleteOneNote(id, token);
		if(result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully deleted", 200));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong can't delete", 400));
		}
		
	}
	
	@PostMapping("/updatenote/{id}")
	@ApiOperation(value = "Api to update Note for fundoonotes" , response = Response.class)
	private ResponseEntity<Response> updateNote(@PathVariable("id") long noteId, @RequestBody NoteDto noteDto, @RequestHeader("token") String token  )
			throws Exception {

		boolean result = noteService.updateNote(noteDto, token ,noteId);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is update successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}
	
@PostMapping("/reminder/{id}")
@ApiOperation(value = "Api to add Reminder Note for fundoonotes" , response = Response.class)
private ResponseEntity<Response> reminder(@PathVariable("id") long noteId, @RequestBody ReminderDto reminderDto, @RequestHeader("token") String token  )
		throws Exception {

	boolean result = noteService.reminder(reminderDto, token ,noteId);
	if (result) {
		return ResponseEntity.status(HttpStatus.OK).body(new Response("reminder is update successfully", 200));
	} else {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
	}
}
}