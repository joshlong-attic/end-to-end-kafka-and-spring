package com.example.basics.movies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

	@JsonProperty("movie_id")
	private int movieId;

	@JsonProperty("title")
	private String title;

	@JsonProperty("country")
	private String country;

	@JsonProperty("genres")
	private String[] genres;

	@JsonProperty("actors")
	private String[] actors;

	@JsonProperty("directors")
	private String[] directors;

	@JsonProperty("composers")
	private String[] composers;

	@JsonProperty("screenwriters")
	private String[] screenWriters;

	@JsonProperty("production_companies")
	private String[] productionCompanies;

	@JsonProperty("cinematographer")
	private String cinematographer;
}
