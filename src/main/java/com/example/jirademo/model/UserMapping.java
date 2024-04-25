package com.example.jirademo.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMapping {

	private String userId;
	private List<Ticket> tickets;
	
	@DynamoDbPartitionKey
	public String getUserId() {
		return userId;
	}
}
