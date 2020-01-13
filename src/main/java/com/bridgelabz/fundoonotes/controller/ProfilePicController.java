package com.bridgelabz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.bridgelabz.fundoonotes.model.Profile;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.ProfileService;

@RestController
public class ProfilePicController {

	@Autowired
	private ProfileService profileService;

	@PostMapping("/uploadprofilepic")
	public ResponseEntity<Response> addProfilePic(@ModelAttribute MultipartFile file,
			@RequestHeader("token") String token) {
		Profile profile = profileService.storeObjectInS3(file, file.getOriginalFilename(), file.getContentType(),
				token);
		return profile.getUserLabel() != null
				? ResponseEntity.status(HttpStatus.OK).body(new Response("profile added succussefully", 200, profile))
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("something went Wrong ", 400));
	}
	
	@PutMapping("/updateprofilepic")
	public ResponseEntity<Response> updateProfile(@ModelAttribute MultipartFile file , @RequestHeader("token") String token){
		Profile profile = profileService.updateObejctInS3(file, file.getOriginalFilename(), file.getContentType(),
				token);
		return profile.getUserLabel() != null
				? ResponseEntity.status(HttpStatus.OK).body(new Response("profile added succussefully", 200, profile))
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("something went Wrong ", 400));
	}
	
	@GetMapping("/getprofilepic")
	public ResponseEntity<Response> getProfilePic(@RequestHeader("token") String token){
	
	S3Object s3 = 	profileService.getProfilePic(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("profile added succussefully", 200, s3));
	}
}
