package com.example.jirademo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.jirademo.model.Ticket;
import com.example.jirademo.model.UserMapping;
import com.example.jirademo.repository.TicketRepository;
import com.example.jirademo.repository.UserMappingRepository;
import com.example.jirademo.constant.StringConstants;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TicketService {

	@Autowired
	private final TicketRepository ticketRepository;
	
	@Autowired
	private final UserMappingRepository userMappingRepository;

    public TicketService(TicketRepository ticketRepository,UserMappingRepository userMappingRepository) {
        this.ticketRepository = ticketRepository;
		this.userMappingRepository = userMappingRepository;
    }
    

    public Mono<String> createNewTicket(Ticket ticket) {
    	
    	ticket.setTicketId(UUID.randomUUID().toString());
        ticket.setStatus("Backlog");
    	userMappingRepository.getUserMappingById(ticket.getAssignee()).thenApply(retrievedUserMapping -> {
            if (null == retrievedUserMapping) {
                throw new IllegalArgumentException(StringConstants.EXCEPTION);
            }
            List<Ticket> ticketList=new ArrayList<Ticket>(retrievedUserMapping.getTickets()); 
            
            ticketList.add(ticket);
           retrievedUserMapping.setTickets(ticketList);
           
           
           return retrievedUserMapping;
       }).thenCompose(ticketList->userMappingRepository.updateUserMapping(ticketList)).join();
      
    	userMappingRepository.getUserMappingById(ticket.getReporter()).thenApply(retrievedUserMapping -> {
            if (null == retrievedUserMapping) {
                throw new IllegalArgumentException(StringConstants.EXCEPTION);
            }
            List<Ticket> ticketList=new ArrayList<Ticket>(retrievedUserMapping.getTickets()); 
            
            ticketList.add(ticket);
           retrievedUserMapping.setTickets(ticketList);
           
           
            return retrievedUserMapping;
        }).thenCompose(ticketList->userMappingRepository.updateUserMapping(ticketList)).join();
       
    	return Mono.fromFuture(ticketRepository.save(ticket))
    			.thenReturn("SUCCESS")
                .onErrorReturn("FAIL" );

    }

    public Mono<Ticket> getTicketByTicketId(String ticketId) {
        Ticket ticket = ticketRepository.getTicketById(ticketId)
        		.thenApply(retrievedTicket -> {
                    if (null == retrievedTicket) {
                        throw new IllegalArgumentException(StringConstants.EXCEPTION);
                    }
                    return retrievedTicket;
                })
               .join();
        return Mono.just(ticket);
    }

    
    public Mono<Ticket> updateExistingTicket(Ticket ticket,String ticketId) {
    	ticket.setTicketId(ticketId);
         Ticket updateStatus = ticketRepository.getTicketById(ticketId)
                 .thenApply(retrievedTicket -> {
                     if (null == retrievedTicket) {
                         throw new IllegalArgumentException(StringConstants.EXCEPTION);
                     }
                     userMappingRepository.getUserMappingById(ticket.getAssignee()).thenApply(retrievedUserMapping -> {
                         if (null == retrievedUserMapping) {
                             throw new IllegalArgumentException(StringConstants.EXCEPTION);
                         }
                        List<Ticket> ticketList=new ArrayList<Ticket>(retrievedUserMapping.getTickets()); 
                        ticketList.remove(retrievedTicket);
                        ticketList.add(ticket);
                        
                        
                        userMappingRepository.updateUserMapping(new UserMapping(retrievedUserMapping.getUserId(),ticketList));
                        
                         return retrievedUserMapping;
                     });
                     userMappingRepository.getUserMappingById(ticket.getReporter()).thenApply(retrievedUserMapping -> {
                         if (null == retrievedUserMapping) {
                             throw new IllegalArgumentException(StringConstants.EXCEPTION);
                         }
                         List<Ticket> ticketList=new ArrayList<Ticket>(retrievedUserMapping.getTickets()); 
                         System.out.println(ticketList);
                          ticketList.remove(retrievedTicket);
                          ticketList.add(ticket);
                          System.out.println(ticketList);
                          userMappingRepository.updateUserMapping(new UserMapping(retrievedUserMapping.getUserId(),ticketList));
                        
                         return retrievedUserMapping;
                     });
                     
                     return retrievedTicket;
                 }).thenCompose(__ -> ticketRepository.updateTicket(ticket))
                .join();
         
     	

        return Mono.just(updateStatus);
    }
    
    public Mono<Ticket> deleteExistingTicket(String ticketId) {
    	
         Ticket updateStatus = ticketRepository.getTicketById(ticketId)
                 .thenApply(retrievedTicket -> {
                     if (null == retrievedTicket) {
                         throw  new  IllegalArgumentException(StringConstants.EXCEPTION);
                     }
                     return retrievedTicket;
                 }).thenCompose(__ -> ticketRepository.deleteTicketById(ticketId))
                .join();

        return Mono.just(updateStatus);
    }
    

    public Mono<Ticket> deleteComment(String ticketId,String comment)
    {
    	Ticket deleteComment= ticketRepository.getTicketById(ticketId)
    			.thenApply(retrievedTicket -> {
                    if (null == retrievedTicket) {
                        throw  new  IllegalArgumentException(StringConstants.EXCEPTION);
                    }
                    if(retrievedTicket.getComments().contains(comment))
                    {
                    	retrievedTicket.getComments().remove(comment);
                    }
                    else
                    {
                    	throw  new  IllegalArgumentException("Invalid Comment");	
                    }
                    return retrievedTicket;
                }).thenCompose(ticket -> ticketRepository.updateTicket(ticket))
               .join();
    	return Mono.just(deleteComment);
    }
    
    
    
  
}

