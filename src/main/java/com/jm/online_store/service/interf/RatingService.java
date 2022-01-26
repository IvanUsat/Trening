package com.jm.online_store.service.interf;


import com.jm.online_store.model.Rating;
import com.jm.online_store.model.dto.RatingDto;

import java.util.List;

public interface RatingService {
    RatingDto findRatingDtoById(Long id);
    RatingDto avgRating(RatingDto ratingDto);
}
