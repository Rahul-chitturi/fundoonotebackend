package com.bridgelabz.fundoonotes.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.servceImplementaion.AwsAdapter;


@RestController
@RequestMapping
public class AwsController {

    @Autowired
    private AwsAdapter awsAdapter;

    @PostMapping("/aws")
    public ResponseEntity<Response> upload(@ModelAttribute MultipartFile file) {
        URL url = awsAdapter.storeObjectInS3(file, file.getOriginalFilename(), file.getContentType());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		.body(new Response("url " + url.toString(), 200)); //new ResponseEntity(HttpStatus.OK, "File uploaded successfully.", new CommonDto("url", url.toString()));
    }

    @GetMapping
    public void getMedicalRecords(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        S3Object s3Object = awsAdapter.fetchObject(fileName);
        InputStream stream = s3Object.getObjectContent();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        IOUtils.copy(stream, response.getOutputStream());
    }
    @DeleteMapping("/delete")
    public String deleteObject(@RequestParam String fileName, HttpServletResponse response) throws IOException {
      awsAdapter.deleteObject(fileName);
return "successe";
    }
}
