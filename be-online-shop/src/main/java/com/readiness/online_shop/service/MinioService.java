package com.readiness.online_shop.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioService {
    @Value("${application.minio.bucket-name}")
    private String BUCKET_NAME ;

    private final Integer EXPIRY = 7200;

    @Autowired
    MinioClient minioClient;

    public String getImageUrl(String imageFileName) throws Exception {
        if(imageFileName == null){
            return null;
        }
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                        .builder()
                        .method(Method.GET)
                        .bucket(this.BUCKET_NAME)
                        .object(imageFileName)
                        .expiry(this.EXPIRY)
                        .build()
        );
    }

    public void uploadFile(MultipartFile file, String filename) throws Exception{
        minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(this.BUCKET_NAME)
                        .object(filename)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }

    public void deleteFile(String filename) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs
                        .builder()
                        .bucket(this.BUCKET_NAME)
                        .object(filename)
                        .build()
        );
    }
}
