package com.ticketing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.domain.MovieDetails;
import com.ticketing.service.AdminService;

@RestController
@RequestMapping(value="/admin")
public class AdminController {
	@Autowired
	AdminService adminService;
	
	@RequestMapping(value="/setMovieDetails",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setTheater(@RequestBody MovieDetails movieDetails){
		adminService.setMovieDetails(movieDetails);
	}
	
	/*@RequestMapping(value="/saveMovie",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void setMovie(@RequestBody Movie movie){
		adminService.saveMovie(movie);
	}*/
}
