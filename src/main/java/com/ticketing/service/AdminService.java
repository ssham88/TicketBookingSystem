package com.ticketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketing.domain.Movie;
import com.ticketing.domain.MovieDetails;
import com.ticketing.domain.Seat;
import com.ticketing.domain.Theater;
import com.ticketing.repository.MovieRepository;
import com.ticketing.repository.SeatRepository;
import com.ticketing.repository.TheaterRepository;

@Service
public class AdminService {
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	TheaterRepository theaterRepository;
	@Autowired
	SeatRepository seatRepository;
	
	public void setMovieDetails(MovieDetails movieDetails) {
		movieDetails.getTheaterDetailsList().forEach(thDetails->{
			Theater theater =  new Theater();
			//theater.setTheaterId(thDetails.getTheaterId());
			theater.setTheaterName(thDetails.getTheaterName());
			theater.setPrice(thDetails.getPrice());
			theater.setLang(thDetails.getLang());
			saveTheater(theater);
			
			Movie movie = new Movie();
			movie.setMovieName(movieDetails.getMovieName());
			movie.setThreaterId(theater.getTheaterId());
			saveMovie(movie);
			
			thDetails.getSeatList().forEach(s->{
				s.setTheaterId(theater.getTheaterId());
				saveSeat(s);
			});
			
		});
	}
	
	public void saveMovie(Movie movie) {
		movieRepository.save(movie);
	}
	
	public void saveSeat(Seat seat) {
		seatRepository.save(seat);
	}
	
	public void saveTheater(Theater theater) {
		theaterRepository.save(theater);
	}
}
