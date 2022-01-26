package com.jm.online_store.controller.rest;

import com.jm.online_store.model.Rating;
import com.jm.online_store.model.dto.RatingDto;
import com.jm.online_store.repository.RatingRepository;
import com.jm.online_store.service.interf.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RatingSetRestController {

    private RatingService ratingService;

    @PostMapping("/setRatingById")
    public RatingDto setRatingById(@RequestBody RatingDto ratingDto) {
        return ratingService.avgRating(ratingDto);
    }
}
