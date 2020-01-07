package com.bridgelabz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDto;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.NoteService;

@RestController
public class NoteController {

	@Autowired
	private NoteService noteService;

	/*
	 * API to create notes
	 */
	@PostMapping("notes/create")
	private ResponseEntity<Response> createNote(@RequestBody NoteDto noteDto, @RequestHeader("token") String token)
			throws Exception {

		boolean result = noteService.computeSave(noteDto, token);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is created successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	@PutMapping("/notes/color/{id}")
	private ResponseEntity<Response> color(@PathVariable("id") long noteId, @RequestParam String color,
			@RequestHeader("token") String token) {

		boolean result = noteService.color(color, token, noteId);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Color changed succussfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}

	}

	@PutMapping("/notes/archive/{id}")
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

	@PutMapping("/notes/pinned/{id}")
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

	@PutMapping("/notes/delete/{id}")
	private ResponseEntity<Response> delete(@PathVariable("id") long noteId, @RequestHeader("token") String token) {
		int result = noteService.delete(token, noteId);
		if (result == 1) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully restored", 200));
		} else if (result == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully moved to trash ", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}

	@DeleteMapping("notes/deleteforever/{id}")
	private ResponseEntity<Response> deleteOneNote(@PathVariable("id") long id, @RequestHeader("token") String token)
			throws Exception {

		boolean result = noteService.deleteOneNote(id, token);
		if(result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("succussfully deleted", 200));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong can't delete", 400));
		}
		
	}
	
	@PostMapping("/notes/updatenote/{id}")
	private ResponseEntity<Response> updateNote(@PathVariable("id") long noteId, @RequestBody NoteDto noteDto, @RequestHeader("token") String token  )
			throws Exception {

		boolean result = noteService.updateNote(noteDto, token ,noteId);
		if (result) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is update successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		}
	}
	
@PostMapping("/notes/reminder/{id}")
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