package com.ticketing.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.domain.ImmutableUser;
import com.ticketing.domain.Movie;
import com.ticketing.domain.MovieDetails;
import com.ticketing.domain.Seat;
import com.ticketing.domain.Ticket;
import com.ticketing.service.UserService;

@RestController
@RequestMapping(value="user")
public class UserController {
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/getMovieDetails/{movieName}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public MovieDetails getMovieDetails(@PathVariable String movieName){
		return userService.getMovieDetails(movieName);
	}
	@RequestMapping(value="/getSeatDetails",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Seat> getSeatDetails(){
		return userService.getSeatDetails();
	}
	@RequestMapping(value="/getMovie",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Movie> getAllMovies(){
		return userService.getAllMovies();
	}
	@RequestMapping(value="/bookMovieTicket",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String bookMovieTicket(@RequestBody Ticket ticket){
		return userService.bookMovieTicket(ticket);
	}
	@RequestMapping(value="/cancelMovieTicket",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String cancelMovieTicket(@RequestBody Ticket ticket){
		return userService.cancelMovieTicket(ticket);
	}
	
	@RequestMapping(value="/registration",method=RequestMethod.POST,produces=MediaType.TEXT_PLAIN_VALUE)
	public String registerUser(@RequestBody ImmutableUser user){
		return userService.registerUser(user);
	}
	
	@RequestMapping(value="/unRegister",method=RequestMethod.POST,produces=MediaType.TEXT_PLAIN_VALUE)
	public String unRegister(@RequestBody ImmutableUser user){
		return userService.unRegister(user);
	}
}
