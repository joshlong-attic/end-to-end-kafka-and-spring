package com.example.basics.ratings;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
class RatingService {

	private final List<Rating> ratings;

	RatingService(ObjectMapper om, Resource cpr ) throws Exception {
		var json = Files.readString(cpr.getFile().toPath());
		var ref = new TypeReference<Collection<Rating>>() { };
		this.ratings = om.readValue(json, ref);
		Assert.notNull(this.ratings, "there are no ratings");
	}

	@SneakyThrows
	Rating generateRandomRating(int stddev) {
		var random = ThreadLocalRandom.current();
		var numberOfTargets = ratings.size();
		var targetIndex = random.nextInt(numberOfTargets);
		var rating = this.ratings.get(targetIndex);
		var randomRating = (random.nextGaussian() * stddev) + rating.getRating();
		var randomRatingRefined = Math.max(Math.min(randomRating, 10), 0);
		return new Rating(ratings.get(targetIndex).getId(), randomRatingRefined);
	}

}
