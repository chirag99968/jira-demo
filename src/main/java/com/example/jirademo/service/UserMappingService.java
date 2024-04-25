package com.example.jirademo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jirademo.model.UserMapping;
import com.example.jirademo.repository.UserMappingRepository;

import reactor.core.publisher.Mono;

@Service
public class UserMappingService {

	@Autowired
	private final UserMappingRepository userMappingRepository;

    public UserMappingService(UserMappingRepository userMappingRepository) {
        this.userMappingRepository =userMappingRepository;
    }
    
    public Mono<String> createNewUserMapping(UserMapping userMapping) {
    	
    	
    	return Mono.fromFuture(userMappingRepository.save(userMapping))
    			.thenReturn("SUCCESS")
                .onErrorReturn("FAIL" );

    }

    public Mono<UserMapping> getUserMappingByUserId(String userId) {
        UserMapping userMapping = userMappingRepository.getUserMappingById(userId)
        		.thenApply(retrievedUserMapping -> {
                    if (null == retrievedUserMapping) {
                        throw new IllegalArgumentException("Invalid UserMappingID");
                    }
                    return retrievedUserMapping;
                })
               .join();
        return Mono.just(userMapping);
    }

    
    public Mono<UserMapping> updateExistingUserMapping(UserMapping userMapping,String userId) {
    	userMapping.setUserId(userId);
         UserMapping updateStatus = userMappingRepository.getUserMappingById(userId)
                 .thenApply(retrievedUserMapping -> {
                     if (null == retrievedUserMapping) {
                         throw new IllegalArgumentException("Invalid UserMappingID");
                     }
                     return retrievedUserMapping;
                 }).thenCompose(__ -> userMappingRepository.updateUserMapping(userMapping))
                .join();

        return Mono.just(updateStatus);
    }
	
}
