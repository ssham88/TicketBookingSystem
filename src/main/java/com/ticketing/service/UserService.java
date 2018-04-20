package com.ticketing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ticketing.domain.ImmutableUser;
import com.ticketing.domain.Movie;
import com.ticketing.domain.MovieDetails;
import com.ticketing.domain.Seat;
import com.ticketing.domain.Theater;
import com.ticketing.domain.TheaterDetails;
import com.ticketing.domain.Ticket;
import com.ticketing.repository.MovieRepository;
import com.ticketing.repository.SeatRepository;
import com.ticketing.repository.TheaterRepository;
import com.ticketing.repository.UserRepository;

import io.reactivex.Observable;

@Service
public class UserService {

	@Autowired
	MovieRepository movieRepository;
	@Autowired
	TheaterRepository threaterRepository;
	@Autowired
	SeatRepository seatRepository;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	UserRepository userRepository;

	private List<TheaterDetails> theaterDetailsList = null;

	public Observable<List<Movie>> getAllMovies() {
		return Observable.just(movieRepository.findAll());
	}

	public Observable<MovieDetails> getMovieDetails(String movieName) {
		MovieDetails movieDetails = new MovieDetails();
		return Observable.just(movieDetails).map(details->{
			Query movieQuery = null;
			Query theaterQuery = null;
			List<Movie> moviesList = null;
			List<String> theaterIdList = null;
			List<Theater> thList = null;
			

			movieQuery = new Query();
			movieQuery.addCriteria(Criteria.where("movieName").is(movieName));
			moviesList = mongoTemplate.find(movieQuery, Movie.class);

			theaterIdList = moviesList.stream().map(m -> m.getThreaterId()).collect(Collectors.toList());
			theaterQuery = new Query();
			theaterQuery.addCriteria(Criteria.where("theaterId").in(theaterIdList));
			thList = mongoTemplate.find(theaterQuery, Theater.class);

			theaterDetailsList = new ArrayList<>();

			thList.forEach(th -> {
				TheaterDetails theaterDetails = new TheaterDetails();
				theaterDetails.setTheaterName(th.getTheaterId());
				theaterDetails.setPrice(th.getPrice());
				theaterDetails.setLang(th.getLang());
				theaterDetails.setSeatList(getSeatList(th));
				theaterDetailsList.add(theaterDetails);
			});

			movieDetails.setMovieName(movieName);
			movieDetails.setTheaterDetailsList(theaterDetailsList);
			return movieDetails;
		});
		
	}

	/*public List<Seat> getSeatDetails() {
		return seatRepository.findAll();
	}*/
	
	public Observable<List<Seat>> getSeatDetails() {
		return Observable.just(seatRepository.findAll());
	}

	public String bookMovieTicket(Ticket ticket) {
		Theater theater = null;
		Seat seat = null;
		String result;
		List<ImmutableUser> immutableUser = null;

		immutableUser = fetchUser(ticket.getUserName());
		if (immutableUser.isEmpty()) {
			return "Please register before booking Ticket.";
		}
		theater = getTheater(ticket.getTheaterName());
		seat = getSeatList(theater).stream().filter(s -> ticket.getSeatNum().equals(s.getSeatNum())).findFirst().get();

		if (seat.getAvailability().equalsIgnoreCase("Y")) {
			updateSeat(seat, "N", ticket.getUserName());
			result = "Seat booked!!";
		} else {
			result = "Seat Not Available!!Please choose another one.";
		}
		return result;
	}

	private List<ImmutableUser> fetchUser(String userName) {
		Query userQuery = new Query();
		userQuery.addCriteria(Criteria.where("name").is(userName));
		return mongoTemplate.find(userQuery, ImmutableUser.class);

	}

	private void updateSeat(Seat seat, String avail, String user) {
		Update update = null;
		Query query = null;
		query = new Query();
		query.addCriteria(Criteria.where("seatNum").is(seat.getSeatNum())
				.andOperator(Criteria.where("theaterId").is(seat.getTheaterId())));
		update = new Update().set("availability", avail).set("userName", user);
		mongoTemplate.upsert(query, update, Seat.class);
	}

	private List<Seat> getSeatList(Theater th) {
		Query seatQuery = null;
		seatQuery = new Query();
		seatQuery.addCriteria(Criteria.where("theaterId").is(th.getTheaterId()));
		List<Seat> seatList = mongoTemplate.find(seatQuery, Seat.class);
		return seatList;
	}

	private Theater getTheater(String theaterName) {
		Query theaterQuery = null;
		theaterQuery = new Query();
		theaterQuery.addCriteria(Criteria.where("theaterId").is(theaterName));
		return mongoTemplate.find(theaterQuery, Theater.class).get(0);
	}

	public String cancelMovieTicket(Ticket ticket) {
		Theater theater = null;
		Seat seat = null;
		String result;
		List<ImmutableUser> immutableUser = null;

		immutableUser = fetchUser(ticket.getUserName());
		if (immutableUser.isEmpty()) {
			return "Please register before cancelling Ticket.";
		}

		theater = getTheater(ticket.getTheaterName());
		seat = getSeatList(theater).stream().filter(s -> ticket.getSeatNum().equals(s.getSeatNum())).findFirst().get();
		if (!seat.getUserName().equalsIgnoreCase(ticket.getUserName())) {
			return "Not Authorized to cancel this Ticket!!";
		}
		if (seat.getAvailability().equalsIgnoreCase("N")) {
			updateSeat(seat, "Y", null);
			result = "Ticket cancelled!!";
		} else {
			result = "Seat is yet to be booked!!";
		}
		return result;
	}

	public String registerUser(ImmutableUser user) {
		if (!fetchUser(user.getName()).isEmpty()) {
			return "Please choose another name.";
		} else {
			userRepository.save(user);
			return "Welcome " + user.getName();
		}
	}

	public String unRegister(ImmutableUser user) {
		Query seatQuery = null;
		if (fetchUser(user.getName()).isEmpty()) {
			return "User is not registered!!";
		} else {
			seatQuery = new Query();
			seatQuery.addCriteria(Criteria.where("userName").is(user.getName()));
			List<Seat> seatList = mongoTemplate.find(seatQuery, Seat.class);
			if(!seatList.isEmpty()) {
				seatList.forEach(seat->{
					updateSeat(seat, "Y", null);
				});
			}
			userRepository.delete(user);
			return "User Unregistered successfully!!";
		}
	}

}
