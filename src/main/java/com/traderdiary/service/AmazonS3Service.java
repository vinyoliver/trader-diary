package com.traderdiary.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.traderdiary.utils.Credentials;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.io.File;
import java.net.URL;
import java.util.List;

@Stateless
public class AmazonS3Service {

    private AmazonS3 s3Client;


    @PostConstruct
    protected void init() {
        BasicAWSCredentials AWS_CREDS = new BasicAWSCredentials(Credentials.S3_ACCESS_KEY, Credentials.SECRET_KEY);
        s3Client = new AmazonS3Client(AWS_CREDS);
        s3Client.setEndpoint(Credentials.END_POINT);//generico end point...
    }

    public void uploadFile(final String filePath, File file) {
        try {
            PutObjectRequest object = new PutObjectRequest(getBucket(), filePath, file);
            object.setCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(object);
        } catch (Exception e) {
            throw e;
        }
    }

    public void delete(String pathToRemove) {
        s3Client.deleteObject(getBucket(), pathToRemove);
    }

    public URL getTempLink(String url) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(getBucket(), url);
        return s3Client.generatePresignedUrl(request);
    }

    private String getBucket() {
        return Credentials.S3_BUCKET;
    }


    public void delete(List<String> key) {
        DeleteObjectsRequest deleteObjectRequest = new DeleteObjectsRequest(getBucket());
        deleteObjectRequest.withKeys(key.toArray(new String[0]));
        try {
            s3Client.deleteObjects(deleteObjectRequest);
        } catch (AmazonServiceException e) {
            //LOGGER.error("Erro deletar:  ");
        }
    }
}
