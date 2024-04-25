package com.example.jirademo.routes;

import com.example.jirademo.model.Ticket;
import com.example.jirademo.model.User;
import com.example.jirademo.model.UserMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import com.example.jirademo.handler.TicketHandler;
import com.example.jirademo.handler.UserHandler;
import com.example.jirademo.handler.UserMappingHandler;

import java.util.List;

@Configuration
@EnableWebFlux
public class JiraRoute {


	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().components(new Components().addSecuritySchemes("bearer-key",
				new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}

	@Bean
	@RouterOperations({
			@RouterOperation(
					path = "/user",
					produces = {
							MediaType.APPLICATION_JSON_VALUE
					},
					method = RequestMethod.GET,
					beanClass = UserHandler.class,
					beanMethod = "getAllUser",
					operation = @Operation(
							operationId = "getAllUser",
							security = { @SecurityRequirement(name = "bearer-key") },
							responses = {
									@ApiResponse(
											responseCode = "200",
											description = "successful",
											content = @Content(
													schema = @Schema(
															implementation = List.class
													)
											)
									),
									@ApiResponse(
											responseCode = "400",
											description = "The request content is invalid"
									),
									@ApiResponse(
											responseCode = "401",
											description = "The request cannot be properly authorized."
									),
									@ApiResponse(
											responseCode = "403",
											description = "The user does not have required permissions to access the resource."
									),
									@ApiResponse(
											responseCode = "404",
											description = "The requested resource does not exist"
									),
									@ApiResponse(
											responseCode = "500",
											description = "The internal server error has occurred"
									)

							}
					)
			),
			@RouterOperation(
					path = "/signup",
					produces = {
							MediaType.APPLICATION_JSON_VALUE
					},
					method = RequestMethod.POST,
					beanClass = UserHandler.class,
					beanMethod = "addUser",
					operation = @Operation(
							operationId = "addUser",
							responses = {
									@ApiResponse(
											responseCode = "200",
											description = "successful",
											content = @Content(
													schema = @Schema(
															implementation = String.class
													)
											)
									),
									@ApiResponse(
											responseCode = "400",
											description = "The request content is invalid"
									),
									@ApiResponse(
											responseCode = "401",
											description = "The request cannot be properly authorized."
									),
									@ApiResponse(
											responseCode = "403",
											description = "The user does not have required permissions to access the resource."
									),
									@ApiResponse(
											responseCode = "404",
											description = "The requested resource does not exist"
									),
									@ApiResponse(
											responseCode = "500",
											description = "The internal server error has occurred"
									),


							},
							requestBody = @RequestBody(
									content = @Content(schema = @Schema(
											implementation = User.class
									)
									)

							)
					))
	})
	 public RouterFunction<ServerResponse> userRoute(UserHandler userHandler)
	 {
		return RouterFunctions
				.route(POST("/signup").and(accept(MediaType.APPLICATION_JSON))
		               ,userHandler::addUser)
                .andRoute(GET("/user").and(accept(MediaType.APPLICATION_JSON))
                        ,userHandler::getAllUser);

	 }

	@Bean
	@RouterOperations({
			@RouterOperation(
					path = "/ticket/{ticketId}",
					produces = {
							MediaType.APPLICATION_JSON_VALUE
					},
					method = RequestMethod.GET,
					beanClass = TicketHandler.class,
					beanMethod = "getTicketById",
					operation = @Operation(
							operationId = "ticketId",
							security = { @SecurityRequirement(name = "bearer-key") },
							responses = {
									@ApiResponse(
											responseCode = "200",
											description = "successful",
											content = @Content(
													schema = @Schema(
															implementation = Ticket.class
													)
											)
									),
									@ApiResponse(
											responseCode = "400",
											description = "The request content is invalid"
									),
									@ApiResponse(
											responseCode = "401",
											description = "The request cannot be properly authorized."
									),
									@ApiResponse(
											responseCode = "403",
											description = "The user does not have required permissions to access the resource."
									),
									@ApiResponse(
											responseCode = "404",
											description = "The requested resource does not exist"
									),
									@ApiResponse(
											responseCode = "500",
											description = "The internal server error has occurred"
									)

							},
							parameters = {
									@Parameter(in= ParameterIn.PATH, name = "ticketId"),
							}

					)
			),
			 @RouterOperation(
					 path = "/ticket",
					 produces = {
							 MediaType.APPLICATION_JSON_VALUE
					 },
					 method = RequestMethod.POST,
					 beanClass = TicketHandler.class,
					 beanMethod = "generateTicket",
					 operation = @Operation(
							 operationId = "generateTicket",
							 security = { @SecurityRequirement(name = "bearer-key") },
							 responses = {
									 @ApiResponse(
											 responseCode = "200",
											 description = "successful",
											 content = @Content(
													 schema = @Schema(
															 implementation = String.class
													 )
											 )
									 ),
									 @ApiResponse(
											 responseCode = "400",
											 description = "The request content is invalid"
									 ),
									 @ApiResponse(
											 responseCode = "401",
											 description = "The request cannot be properly authorized."
									 ),
									 @ApiResponse(
											 responseCode = "403",
											 description = "The user does not have required permissions to access the resource."
									 ),
									 @ApiResponse(
											 responseCode = "404",
											 description = "The requested resource does not exist"
									 ),
									 @ApiResponse(
											 responseCode = "500",
											 description = "The internal server error has occurred"
									 ),


							 },
							 requestBody = @RequestBody(
									 content = @Content(schema = @Schema(
											 implementation = Ticket.class
									 )
							 )
					 )
			 )),
			 @RouterOperation(
					 path = "/ticket/{ticketId}",
					 produces = {
							 MediaType.APPLICATION_JSON_VALUE
					 },
					 method = RequestMethod.DELETE,
					 beanClass = TicketHandler.class,
					 beanMethod = "deleteTicketById",
					 operation = @Operation(
							 operationId = "ticketId",
							 security = { @SecurityRequirement(name = "bearer-key") },
							 responses = {
									 @ApiResponse(
											 responseCode = "200",
											 description = "successful",
											 content = @Content(
													 schema = @Schema(
															 implementation = Ticket.class
													 )
											 )
									 ),
									 @ApiResponse(
											 responseCode = "400",
											 description = "The request content is invalid"
									 ),
									 @ApiResponse(
											 responseCode = "401",
											 description = "The request cannot be properly authorized."
									 ),
									 @ApiResponse(
											 responseCode = "403",
											 description = "The user does not have required permissions to access the resource."
									 ),
									 @ApiResponse(
											 responseCode = "404",
											 description = "The requested resource does not exist"
									 ),
									 @ApiResponse(
											 responseCode = "500",
											 description = "The internal server error has occurred"
									 )

							 },
							 parameters = {
									 @Parameter(in= ParameterIn.PATH, name = "ticketId")
							 }
					 )
			 ),
			 @RouterOperation(
					 path = "/ticket/{ticketId}",
					 produces = {
							 MediaType.APPLICATION_JSON_VALUE
					 },
					 method = RequestMethod.PUT,
					 beanClass = TicketHandler.class,
					 beanMethod = "updateTicketById",
					 operation = @Operation(
							 operationId = "ticketId",
							 security = { @SecurityRequirement(name = "bearer-key") },
							 responses = {
									 @ApiResponse(
											 responseCode = "200",
											 description = "successful",
											 content = @Content(
													 schema = @Schema(
															 implementation = Ticket.class
													 )
											 )
									 ),
									 @ApiResponse(
											 responseCode = "400",
											 description = "The request content is invalid"
									 ),
									 @ApiResponse(
											 responseCode = "401",
											 description = "The request cannot be properly authorized."
									 ),
									 @ApiResponse(
											 responseCode = "403",
											 description = "The user does not have required permissions to access the resource."
									 ),
									 @ApiResponse(
											 responseCode = "404",
											 description = "The requested resource does not exist"
									 ),
									 @ApiResponse(
											 responseCode = "500",
											 description = "The internal server error has occurred"
									 )

							 },
							 parameters = {
									 @Parameter(in= ParameterIn.PATH, name = "ticketId")
							 }
					 )
			 )
	 })
	 public RouterFunction<ServerResponse> ticketRoute(TicketHandler ticketHandler){
	return RouterFunctions
			.route(GET("/ticket/{ticketId}").and(accept(MediaType.APPLICATION_JSON))
					,ticketHandler::getTicketById)
			.andRoute(POST("/ticket").and(accept(MediaType.APPLICATION_JSON))
					,ticketHandler::generateTicket)
			.andRoute(DELETE("/ticket/{ticketId}").and(accept(MediaType.APPLICATION_JSON))
					,ticketHandler::deleteTicketById)
			.andRoute(DELETE("/ticket/{ticketId}/{comment}").and(accept(MediaType.APPLICATION_JSON))
					,ticketHandler::deleteCommentById)
			.andRoute(PUT("/ticket/{ticketId}").and(accept(MediaType.APPLICATION_JSON))
					,ticketHandler::updateTicketById);
	 }
	 
	 @Bean
	 @RouterOperations({
			 @RouterOperation(
					 path = "/user/{userId}",
					 produces = {
							 MediaType.APPLICATION_JSON_VALUE
					 },
					 method = RequestMethod.GET,
					 beanClass = UserMappingHandler.class,
					 beanMethod = "getUserMappingById",
					 operation = @Operation(
							 operationId = "userId",
							 security = { @SecurityRequirement(name = "bearer-key") },
							 responses = {
									 @ApiResponse(
											 responseCode = "200",
											 description = "successful",
											 content = @Content(
													 schema = @Schema(
															 implementation = UserMapping.class
													 )
											 )
									 ),
									 @ApiResponse(
											 responseCode = "400",
											 description = "The request content is invalid"
									 ),
									 @ApiResponse(
											 responseCode = "401",
											 description = "The request cannot be properly authorized."
									 ),
									 @ApiResponse(
											 responseCode = "403",
											 description = "The user does not have required permissions to access the resource."
									 ),
									 @ApiResponse(
											 responseCode = "404",
											 description = "The requested resource does not exist"
									 ),
									 @ApiResponse(
											 responseCode = "500",
											 description = "The internal server error has occurred"
									 )

							 },
							 parameters = {
									 @Parameter(in = ParameterIn.PATH, name = "userId")
							 }
					 )
			 )
	 })
	 public RouterFunction<ServerResponse> userMappingRoute(UserMappingHandler userMappingHandler){
	 return RouterFunctions
			.route(GET("/user/{userId}").and(accept(MediaType.APPLICATION_JSON))
					,userMappingHandler::getUserMappingById)
			.andRoute(PUT("/user/{userId}").and(accept(MediaType.APPLICATION_JSON))
					,userMappingHandler::updateUserMappingById);
	 }
}
