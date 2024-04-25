package com.example.jirademo.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.example.jirademo.security.PBKDF2Encoder;
import com.example.jirademo.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.jirademo.model.Ticket;
import com.example.jirademo.model.User;
import com.example.jirademo.model.UserMapping;
import com.example.jirademo.repository.UserMappingRepository;
import com.example.jirademo.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private final UserRepository userRepository;

	 @Autowired
	 private final UserMappingRepository userMappingRepository;

     private PBKDF2Encoder passwordEncoder;

	 
    public UserService(UserRepository userRepository,UserMappingRepository userMappingRepository,PBKDF2Encoder passwordEncoder) {
        this.userRepository = userRepository;
		this.userMappingRepository =  userMappingRepository;
        this.passwordEncoder=passwordEncoder;
    }
    
    public Mono<String> createNewUser(User user) {

        user.setEnabled(true);
        user.setRoles(Arrays.asList(Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMappingRepository.save(new UserMapping(user.getUsername(),Collections.<Ticket>emptyList()));

       	return Mono.fromFuture(userRepository.save(user))
                .thenReturn("SUCCESS")
                .onErrorReturn("FAIL" );

    }

    public Mono<List<String>> getAllUser() {
        return Flux.from(userRepository.getAllUser().items().map(user -> user.getUsername())).collectList()
                .onErrorReturn(null);
    }

    public Mono<User> findByUsername(String username) {
        return Mono.fromFuture(userRepository.getUserByUsername(username))
                .doOnSuccess(Objects::requireNonNull)
                .onErrorReturn(new User());
    }
}
