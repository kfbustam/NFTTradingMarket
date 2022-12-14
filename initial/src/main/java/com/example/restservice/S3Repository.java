package com.example.restservice;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Component
public class S3Repository {
    private final AWSCredentials credentials = new BasicAWSCredentials(
            "AKIA3FBSRKE6BXOAIMCS",
            "8V7g65mJgM8RdpUkNL3TaeR8C3POAKAfNKlZs4qg"
    );

    private final AmazonS3 s3client;

    private final String bucketName;

    public S3Repository() {
         this.s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();

        this.bucketName = "275-nft-bucket";

        if(!s3client.doesBucketExist(bucketName)) {
            s3client.createBucket(bucketName);
        }
    }

    public String uploadFile(MultipartFile multipartFile) throws IOException {

        String fileUrl = "";
        String fileName = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileName = generateFileName(multipartFile);
            fileUrl = "https://s3.us-east-2.amazonaws.com" + "/" + bucketName + "/" + fileName;
            saveImage(fileName, file);
            file.delete();
        } catch (Exception e) {
            throw e;
        }
        return fileName;
    }

    private void saveImage(String fileName, File file) throws IOException {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public InputStream getImage(String fileName) throws IOException {
        S3Object s3object = s3client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        return inputStream;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


}
