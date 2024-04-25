package com.example.jirademo.repository;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.example.jirademo.model.Ticket;
import com.example.jirademo.model.UserMapping;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserMappingRepository {

	private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncTable<UserMapping> userMappingDynamoDbAsyncTable;

    public UserMappingRepository(DynamoDbEnhancedAsyncClient asyncClient) {
        this.enhancedAsyncClient = asyncClient;
        this.userMappingDynamoDbAsyncTable = enhancedAsyncClient.table(UserMapping.class.getSimpleName(), TableSchema.fromBean(UserMapping.class));
    }
    
    public CompletableFuture<Void> save(UserMapping userMapping) {
    	    	return userMappingDynamoDbAsyncTable.putItem(userMapping);
    }
    
    public CompletableFuture<UserMapping> getUserMappingById(String userId) {
    	return userMappingDynamoDbAsyncTable.getItem(getKeyBuild(userId));
    }
    
    public CompletableFuture<UserMapping> updateUserMapping(UserMapping userMapping) {
    	
        return userMappingDynamoDbAsyncTable.updateItem(userMapping);
    }
    
    private Key getKeyBuild(String userId) {
        return Key.builder().partitionValue(userId).build();
    }
	
}
