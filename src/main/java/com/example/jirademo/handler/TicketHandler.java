package com.example.jirademo.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.jirademo.model.Ticket;
import com.example.jirademo.service.TicketService;

import reactor.core.publisher.Mono;

@Component
public class TicketHandler {
	
	 @Autowired
	 private TicketService ticketService;
	 
	 static Mono<ServerResponse> notFound = ServerResponse.notFound().build();
	    
	 public Mono<ServerResponse> getTicketById(ServerRequest serverRequest) {
	    	 
	 String ticketId = serverRequest.pathVariable("ticketId");
	 return ServerResponse.ok()
    		.contentType(MediaType.APPLICATION_JSON)
    		.body(ticketService.getTicketByTicketId(ticketId), Ticket.class);
	}
	     
	public Mono<ServerResponse> updateTicketById(ServerRequest serverRequest) {
	    Mono<Ticket> saveTicket = serverRequest.bodyToMono(Ticket.class);
	    String ticketId = serverRequest.pathVariable("ticketId");
    	return  saveTicket.flatMap(ticket ->
    	    	ServerResponse.ok()
    	          .contentType(MediaType.APPLICATION_JSON)
    	          .body(ticketService.updateExistingTicket(ticket,ticketId), Ticket.class));
	}
	     
	public Mono<ServerResponse> deleteTicketById(ServerRequest serverRequest) {
	    	 
	    String ticketId = serverRequest.pathVariable("ticketId");
    	return   ServerResponse.ok()
    	           .contentType(MediaType.APPLICATION_JSON)
    	           .body(ticketService.deleteExistingTicket(ticketId), Ticket.class);
	}
	     
	public Mono<ServerResponse> deleteCommentById(ServerRequest serverRequest) {
	    	 
	   String ticketId = serverRequest.pathVariable("ticketId");
	   String comment = serverRequest.pathVariable("comment");
       return   ServerResponse.ok()
    	           .contentType(MediaType.APPLICATION_JSON)
    	           .body(ticketService.deleteComment(ticketId,comment), Ticket.class);
	}
	
	public Mono<ServerResponse> generateTicket(ServerRequest serverRequest) {
	    	 
	   Mono<Ticket> saveTicket = serverRequest.bodyToMono(Ticket.class);
	   return saveTicket.flatMap(ticket ->
	    	  ServerResponse.ok()
	    	     .contentType(MediaType.APPLICATION_JSON)
	    	     .body(ticketService.createNewTicket(ticket), Ticket.class));
	}
     
}
