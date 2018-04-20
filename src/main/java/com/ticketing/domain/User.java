package com.ticketing.domain;

import org.immutables.value.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value.Immutable
@Document(collection = "User")
public interface User {
	@Id
	abstract String getName();
	abstract int getAge();
	
}
