package com.example.jirademo.config;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;


import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

/*@Configuration
public class DynamoDbConfig {

    
    @Value("${aws.dynamodb.endpoint}")
    private String dynamoDbEndPointUrl;
    
    @Bean
    public DynamoDbAsyncClient getDynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create("default"))
                .endpointOverride(URI.create(dynamoDbEndPointUrl))
                .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient() {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(getDynamoDbAsyncClient())
                .build();
    }

}*/
@Configuration
public class DynamoDbConfig {


    private final String dynamoDbEndPointUrl;
    private String accessKey ;
    private  String secretkey ;
    private  String region;

    public DynamoDbConfig(@Value("${aws.dynamodb.endpoint}") String dynamoDbEndPointUrl) {
        this.dynamoDbEndPointUrl = dynamoDbEndPointUrl;
        this.accessKey = System.getenv("KEY");
        this.secretkey = System.getenv("SECRET");

    }




    @Bean
    public DynamoDbAsyncClient getDynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        accessKey,
                        secretkey)))
                //.credentialsProvider(ProfileCredentialsProvider.create("default"))
                .endpointOverride(URI.create(dynamoDbEndPointUrl))
                .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient() {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(getDynamoDbAsyncClient())
                .build();
    }
}

