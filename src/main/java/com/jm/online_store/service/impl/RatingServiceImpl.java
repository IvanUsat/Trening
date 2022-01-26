package com.jm.online_store.service.impl;

import com.jm.online_store.model.Order;
import com.jm.online_store.model.Rating;
import com.jm.online_store.model.dto.OrderDTO;
import com.jm.online_store.model.dto.RatingDto;
import com.jm.online_store.repository.RatingRepository;
import com.jm.online_store.service.interf.RatingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;


    @Override
    public RatingDto findRatingDtoById(Long idManager){
        Rating rating = ratingRepository.findAllByUserId(idManager);
        return convertRatingToRatingDto(rating) ;

    }

    /**
     * метод конвертации Order в OrderDTO для отсекания лишних данных.
     */
    private RatingDto convertRatingToRatingDto(Rating rating) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(rating, RatingDto.class);
    }
    /**
     * метод построения OrderDTO из Order, полученного из БД.
     */

    private Rating convertRatingDtoToRating(RatingDto ratingDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(ratingDto, Rating.class);
    }
    @Override
    public RatingDto avgRating(RatingDto ratingDto){
        RatingDto ratingDto1 = findRatingDtoById(ratingDto.getIdManager());
        Rating rating = convertRatingDtoToRating(ratingDto1);
        rating.setRating(ratingDto.getScore().stream().filter(x -> x > 0).filter(x -> x <= 10).mapToDouble(x -> x).average().orElse(0.0));
       if (rating != null){
           ratingRepository.updateOrSave(ratingDto1);
       }
        ratingRepository.save(rating);
        return convertRatingToRatingDto(rating);
    }

}
