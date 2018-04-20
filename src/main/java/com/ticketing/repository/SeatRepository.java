package com.ticketing.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ticketing.domain.Seat;

public interface SeatRepository extends MongoRepository<Seat,String>{

}
