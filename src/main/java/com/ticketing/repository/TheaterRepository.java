package com.ticketing.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ticketing.domain.Theater;

public interface TheaterRepository extends MongoRepository<Theater,String>{
}
