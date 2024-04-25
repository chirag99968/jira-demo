package com.example.jirademo.handler;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.jirademo.model.User;
import com.example.jirademo.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class UserHandler {
	
	 @Autowired
	 private UserService userService;
	 
	 static Mono<ServerResponse> notFound = ServerResponse.notFound().build();
     
     public Mono<ServerResponse> addUser(ServerRequest serverRequest) {
    	 Mono<User> saveUser = serverRequest.bodyToMono(User.class);
 	    	return saveUser.flatMap(user ->
 	    			ServerResponse.ok()
    	            .contentType(MediaType.APPLICATION_JSON)
                    .body(userService.createNewUser(user), User.class));
     }

	public Mono<ServerResponse> getAllUser(ServerRequest serverRequest) {
		return 	ServerResponse.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(userService.getAllUser(), List.class);
	}
     

}
