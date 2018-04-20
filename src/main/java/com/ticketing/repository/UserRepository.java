package com.ticketing.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ticketing.domain.ImmutableUser;

public interface UserRepository extends MongoRepository<ImmutableUser,String>{

}
