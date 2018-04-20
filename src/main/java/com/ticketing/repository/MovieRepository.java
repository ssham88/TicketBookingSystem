package com.ticketing.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ticketing.domain.Movie;

public interface MovieRepository extends MongoRepository<Movie,String>{

}
