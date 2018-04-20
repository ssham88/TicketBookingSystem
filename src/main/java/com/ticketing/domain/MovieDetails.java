package com.ticketing.domain;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MovieDetails {
	
	private String movieName;
	private List<TheaterDetails> theaterDetailsList;
	
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public List<TheaterDetails> getTheaterDetailsList() {
		return theaterDetailsList;
	}
	public void setTheaterDetailsList(List<TheaterDetails> theaterDetailsList) {
		this.theaterDetailsList = theaterDetailsList;
	}
	
}
