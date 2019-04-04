package com.example.basics.ratings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Rating {
	private Integer id;
	private double rating;
}
