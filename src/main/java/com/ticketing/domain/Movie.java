package com.ticketing.domain;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Movies")
@CompoundIndex(name = "compound_index", def = "{'movieName': 1, 'threaterId': 1 }",unique = true)
public class Movie {
	private String movieName;
	private String threaterId;
	
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getThreaterId() {
		return threaterId;
	}
	public void setThreaterId(String threaterId) {
		this.threaterId = threaterId;
	}
}
