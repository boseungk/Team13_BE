package com.theocean.fundering.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.ProxyConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class AWSS3Config {

    @Bean
    public S3Client s3Client() {
        final SdkHttpClient sdkHttpClient = ApacheHttpClient.builder()
                .proxyConfiguration(ProxyConfiguration.builder().endpoint(URI.create("http://krmp-proxy.9rum.cc:3128"))
                        .build())
                .build();
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .httpClient(sdkHttpClient)
                .build();
    }
}
