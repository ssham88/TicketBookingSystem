package com.ticketing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
	@Autowired
	MongoTemplate mongoTemplate;
	
	public String setMovieDetails(MovieDetails movieDetails) {
		movieDetails.getTheaterDetailsList().forEach(thDetails->{
			Theater theater =  new Theater();
			theater.setTheaterId(thDetails.getTheaterName());
			theater.setPrice(thDetails.getPrice());
			theater.setLang(thDetails.getLang());
			if(fetchTheater(thDetails.getTheaterName()).isEmpty()) {
				saveTheater(theater);
				
				Movie movie = new Movie();
				movie.setMovieName(movieDetails.getMovieName());
				movie.setThreaterId(theater.getTheaterId());
				saveMovie(movie);
				
				thDetails.getSeatList().forEach(s->{
					s.setTheaterId(theater.getTheaterId());
					saveSeat(s);
				});
			}
		});
		return "Movie successfully set in Theater.";
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
	
	private List<Theater> fetchTheater(String theaterName) {
		Query theaterQuery = new Query();
		theaterQuery.addCriteria(Criteria.where("theaterId").is(theaterName));
		return mongoTemplate.find(theaterQuery, Theater.class);

	}
}
