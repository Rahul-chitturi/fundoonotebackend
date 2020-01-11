package com.bridgelabz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.servceImplementaion.AwsAdapter;

@RestController
public class ProfilePicController {
	 @Autowired
	    private AwsAdapter awsAdapter;
	 
	 @PostMapping("/uploadProfilepic")
	 public ResponseEntity<Response> addProfilePic(@ModelAttribute MultipartFile file ,  @RequestHeader("token") String token){
	
		 return null;
	 }
}
