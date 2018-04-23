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
import io.reactivex.functions.Function;

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

	public Observable<Movie> getAllMovies() {
		/*return Observable.just(movieRepository.findAll())
        .flatMap(new Function<List<Movie>, Observable<Movie>>() {
            @Override
            public Observable<Movie> apply(List<Movie> movieList) {
                return Observable.fromIterable(movieList);
            }
        });*/
		return Observable.fromIterable(movieRepository.findAll());
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
				theaterDetails.setSeatList(getSeatList(th).toList().blockingGet());
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
	
	public Observable<Seat> getSeatDetails() {
		return Observable.just(seatRepository.findAll())
				.flatMap(new Function<List<Seat>,Observable<Seat>>(){

					@Override
					public Observable<Seat> apply(List<Seat> seatList) throws Exception {
						return Observable.fromIterable(seatList);
					}
					
				});
	}

	public String bookMovieTicket(Ticket ticket) {
		Theater theater = null;
		Seat seat = null;
		String result;

		if (fetchUser(ticket.getUserName()).equals(Observable.empty())) {
			return "Please register before booking Ticket.";
		}
		theater = getTheater(ticket.getTheaterName()).blockingSingle();
		seat= getSeatList(theater).filter(s -> ticket.getSeatNum().equals(s.getSeatNum())).blockingFirst();
		if (seat.getAvailability().equalsIgnoreCase("Y")) {
			updateSeat(seat, "N", ticket.getUserName());
			result = "Seat booked!!";
		} else {
			result = "Seat Not Available!!Please choose another one.";
		}
		return result;
	}

	private Observable<ImmutableUser> fetchUser(String userName) {
		List<ImmutableUser> immutableUserList = null;
		Query userQuery = new Query();
		userQuery.addCriteria(Criteria.where("name").is(userName));
		immutableUserList = mongoTemplate.find(userQuery, ImmutableUser.class);
		return immutableUserList.isEmpty() ? Observable.empty() : Observable.fromIterable(immutableUserList);
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

	private Observable<Seat> getSeatList(Theater th) {
		Query seatQuery = null;
		seatQuery = new Query();
		seatQuery.addCriteria(Criteria.where("theaterId").is(th.getTheaterId()));
		return Observable.just(mongoTemplate.find(seatQuery, Seat.class))
				.flatMap(new Function<List<Seat>,Observable<Seat>>(){

					@Override
					public Observable<Seat> apply(List<Seat> seatList) throws Exception {
						return Observable.fromIterable(seatList);
					}
					
				});
	}

	private Observable<Theater> getTheater(String theaterName) {
		Query theaterQuery = null;
		theaterQuery = new Query();
		theaterQuery.addCriteria(Criteria.where("theaterId").is(theaterName));
		return Observable.fromIterable(mongoTemplate.find(theaterQuery, Theater.class));
	}

	public String cancelMovieTicket(Ticket ticket) {
		Theater theater = null;
		Seat seat = null;
		String result;
		if (fetchUser(ticket.getUserName()).equals(Observable.empty())) {
			return "Please register before cancelling Ticket.";
		}

		theater = getTheater(ticket.getTheaterName()).blockingFirst();
		seat = getSeatList(theater).filter(s -> ticket.getSeatNum().equals(s.getSeatNum())).blockingFirst();
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
		if (!fetchUser(user.getName()).equals(Observable.empty())) {
			return "Please choose another name.";
		} else {
			userRepository.save(user);
			return "Welcome " + user.getName();
		}
	}

	public String unRegister(ImmutableUser user) {
		Query seatQuery = null;
		if (fetchUser(user.getName()).equals(Observable.empty())) {
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
