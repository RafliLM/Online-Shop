package com.readiness.online_shop.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${application.minio.url}")
    private String MINIO_URL;

    @Value("${application.minio.access-key}")
    private String MINIO_ACCESS_KEY;

    @Value("${application.minio.secret-key}")
    private String MINIO_SECRET_KEY;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient
                .builder()
                .endpoint(MINIO_URL)
                .credentials(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)
                .build();
    }
}
