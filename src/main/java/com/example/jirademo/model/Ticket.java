package com.example.jirademo.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
	
	private String ticketId;
	private String title;
	private String description;
	private String assignee;
	private String reporter;
	private String status;
	private String createdDate;
	private String updatedDate;
	private List<String> comments;
	
	@DynamoDbPartitionKey
	public String getTicketId() {
		return ticketId;
	}

}
