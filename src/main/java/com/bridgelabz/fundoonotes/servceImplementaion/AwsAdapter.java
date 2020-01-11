package com.bridgelabz.fundoonotes.servceImplementaion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;



@Component
public class AwsAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AwsAdapter.class);

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private  AmazonS3 amazonS3Client;

    public URL storeObjectInS3(MultipartFile file, String fileName, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(file.getSize());
        //TODO add cache control
        try {
            amazonS3Client.putObject(bucketName, fileName,file.getInputStream(), objectMetadata);
        } catch(AmazonClientException |IOException exception) {
            throw new RuntimeException("Error while uploading file.");
        }
        return amazonS3Client.getUrl(bucketName, fileName);
    }
    public S3Object fetchObject1(String awsFileName) {
    System.out.format("Downloading");
  //  final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
    try {
       
        ListObjectsV2Result result = amazonS3Client.listObjectsV2("fundoonotesprofile");
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            System.out.println("* " + os.getKey());
        }
    } catch (AmazonServiceException e) {
        System.err.println(e.getErrorMessage());
   
    } 
	return null;
    }
    public S3Object fetchObject(String awsFileName) {
        S3Object s3Object;
        try {
            s3Object = amazonS3Client.getObject(new GetObjectRequest(bucketName, awsFileName));
        }catch (AmazonServiceException serviceException) {
        	serviceException.printStackTrace();
        	
            throw new RuntimeException("Error while streaming File.");
        } catch (AmazonClientException exception) {
        	exception.printStackTrace();
            throw new RuntimeException("Error while streaming File.");
        }
        return s3Object;
    }

    public void deleteObject(String key) {
        try {
            amazonS3Client.deleteObject(bucketName, key);
        }catch (AmazonServiceException serviceException) {
            logger.error(serviceException.getErrorMessage());
        } catch (AmazonClientException exception) {
            logger.error("Error while deleting File.");
        }
    }

}