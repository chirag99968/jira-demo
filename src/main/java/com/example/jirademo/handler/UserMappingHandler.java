package com.example.jirademo.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.jirademo.model.UserMapping;
import com.example.jirademo.service.UserMappingService;

import reactor.core.publisher.Mono;

@Component
public class UserMappingHandler {
	
	 @Autowired
	 private UserMappingService userMappingService;
	 
	 static Mono<ServerResponse> notFound = ServerResponse.notFound().build();
	    
     public Mono<ServerResponse> getUserMappingById(ServerRequest serverRequest) {
    	 
    	 String userMappingId = serverRequest.pathVariable("userId");
	     return ServerResponse.ok()
	              .contentType(MediaType.APPLICATION_JSON)
	              .body(userMappingService.getUserMappingByUserId(userMappingId), UserMapping.class);
     }
     
     public Mono<ServerResponse> updateUserMappingById(ServerRequest serverRequest) {
    	 Mono<UserMapping> saveUserMapping = serverRequest.bodyToMono(UserMapping.class);
    	 String userMappingId = serverRequest.pathVariable("userId");
	     return  saveUserMapping.flatMap(userMapping ->
	    		 ServerResponse.ok()
	              .contentType(MediaType.APPLICATION_JSON)
	              .body(userMappingService.updateExistingUserMapping(userMapping,userMappingId), UserMapping.class));
     }
     
     

     
     public Mono<ServerResponse> generateUserMapping(ServerRequest serverRequest) {
    	 
    	 Mono<UserMapping> saveUserMapping = serverRequest.bodyToMono(UserMapping.class);
    	 return saveUserMapping.flatMap(userMapping ->
    	        ServerResponse.ok()
    	           .contentType(MediaType.APPLICATION_JSON)
    	           .body(userMappingService.createNewUserMapping(userMapping), UserMapping.class));
 	  }
     
}
