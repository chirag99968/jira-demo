package com.example.jirademo.repository;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.example.jirademo.model.User;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

@Repository
public class UserRepository {

		private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
	    private final DynamoDbAsyncTable<User> userDynamoDbAsyncTable;

	    public UserRepository(DynamoDbEnhancedAsyncClient asyncClient) {
	        this.enhancedAsyncClient = asyncClient;
	        this.userDynamoDbAsyncTable = enhancedAsyncClient.table(User.class.getSimpleName(), TableSchema.fromBean(User.class));
	    }
	    
	        
	    public CompletableFuture<Void> save(User user)
	    {
	    	return userDynamoDbAsyncTable.putItem(user);
	    }
	    
	    public CompletableFuture<User> getUserByUsername(String username)
	    {
	    	return userDynamoDbAsyncTable.getItem(getKeyBuild(username));
	    }

		public PagePublisher<User> getAllUser() {
		return userDynamoDbAsyncTable.scan();
		}
	    
	    public CompletableFuture<User> updateUser(User user) {
	    	
	        return userDynamoDbAsyncTable.updateItem(user);
	    }
	         
	     	    
	    private Key getKeyBuild(String username) {
	        return Key.builder().partitionValue(username).build();
	    }
	    
}
