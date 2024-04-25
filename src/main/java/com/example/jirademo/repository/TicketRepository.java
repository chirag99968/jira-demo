package com.example.jirademo.repository;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.example.jirademo.model.Ticket;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

@Repository
public class TicketRepository {

	private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncTable<Ticket> ticketDynamoDbAsyncTable;

    public TicketRepository(DynamoDbEnhancedAsyncClient asyncClient) {
        this.enhancedAsyncClient = asyncClient;
        this.ticketDynamoDbAsyncTable = enhancedAsyncClient.table(Ticket.class.getSimpleName(), TableSchema.fromBean(Ticket.class));
    }
    
        
    public CompletableFuture<Void> save(Ticket ticket)
    {
    	
    	
    	CompletableFuture<Void> a=ticketDynamoDbAsyncTable.putItem(ticket);
    	System.out.print(a);
    	return a;
    }
    
    public CompletableFuture<Ticket> getTicketById(String ticketId)
    {
    	return ticketDynamoDbAsyncTable.getItem(getKeyBuild(ticketId));
    }
    
           
    public CompletableFuture<Ticket> updateTicket(Ticket ticket) {
    	
        return ticketDynamoDbAsyncTable.updateItem(ticket);
    }

   
    public CompletableFuture<Ticket> deleteTicketById(String ticketId) {
        return ticketDynamoDbAsyncTable.deleteItem(getKeyBuild(ticketId));
    }
    
    private Key getKeyBuild(String ticketId) {
        return Key.builder().partitionValue(ticketId).build();
    }
    
}






	
    

