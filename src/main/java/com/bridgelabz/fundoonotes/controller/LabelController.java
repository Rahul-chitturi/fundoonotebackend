package com.bridgelabz.fundoonotes.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.LabelService;

@RestController
@RequestMapping("lables")
public class LabelController {

	@Autowired
	private LabelService labelService;

	@PostMapping("/create")
	public ResponseEntity<Response> createLabel(@Valid @RequestBody LabelDto labelDto, @RequestHeader("token") String token ,  BindingResult bindingResult)
		 {
          if (bindingResult.hasErrors()) {
        	  return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(bindingResult.getAllErrors().get(0).getDefaultMessage(), 400));
          }
		Label result = labelService.createlabel(labelDto, token);
		return result!=null ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is Created", 200 ))
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Already exist in lable ", 400 ));

	}


	@PostMapping("/maplabel/{noteId}")
	public ResponseEntity<Response> labelMapToNote(@RequestBody LabelDto labelDto, @PathVariable("noteId") Long noteId,
			@RequestHeader("token") String token) {
		Label result = labelService.createOrMapWithNote(labelDto, noteId, token);
		return result!=null ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is created successfully", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new Response("The label you are trying to create is already exist!!!", 400));
	}

	@DeleteMapping("/remove")
	public ResponseEntity<Response> removeLabel(@RequestHeader("token") String token,
			@RequestParam("noteId") long noteId, @RequestParam("labelId") long labelId) {
		boolean result = labelService.removeLabels(token, noteId, labelId);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is Removed Successfully", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}


	@DeleteMapping("/delete")
	public ResponseEntity<Response> deletelabel(@RequestHeader("token") String token,
			@RequestParam("labelId") long labelId) throws Exception {
		boolean result = labelService.deletepermanently(token, labelId);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is deleted successfully", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new Response("The label you are trying to delete is not Available", 400));
	}


	@PutMapping("/update")
	public ResponseEntity<Response> updateLabel(@RequestHeader("token") String token,
			@RequestParam("labelId") long labelId, @RequestBody LabelDto labelDto) throws Exception {
		boolean result = labelService.updateLabel(token, labelId, labelDto);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is updated successfully", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new Response("The label you are trying to update is not available", 400));
	}


	@PostMapping("addlabels")
	public ResponseEntity<Response> addLabelToNotes(@RequestHeader("token") String token,
			@RequestParam("noteId") long noteId, @RequestParam("labelid") long labelId) {
		boolean result = labelService.addLabels(token, noteId, labelId);
		return (result) ? ResponseEntity.status(HttpStatus.OK).body(new Response("Label is added to the notes", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Something went wrong", 400));
	}
}