package com.jm.online_store.service.impl;

import com.jm.online_store.model.Rating;
import com.jm.online_store.model.dto.RatingDto;
import com.jm.online_store.repository.RatingRepository;
import com.jm.online_store.service.interf.RatingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    @Override
    public RatingDto findRatingDtoById(Long idManager) {
        Rating rating = ratingRepository.findAllByUserId(idManager);
        return convertRatingToRatingDto(rating);

    }

    /**
     * метод конвертации Order в OrderDTO для отсекания лишних данных.
     */
    private RatingDto convertRatingToRatingDto(Rating rating) {
        return modelMapper.map(rating, RatingDto.class);
    }

    /**
     * метод построения OrderDTO из Order, полученного из БД.
     */

    private Rating convertRatingDtoToRating(RatingDto ratingDto) {
        return modelMapper.map(ratingDto, Rating.class);
    }

    @Override
    @Transactional
    public RatingDto avgRating(RatingDto ratingDto) {
        ratingDto.setRating(ratingDto.getScore().stream().filter(x -> x > 0).filter(x -> x <= 10).mapToDouble(x -> x).average().orElse(0.0));
        ratingRepository.saveByUserId(ratingDto.getRating(), ratingDto.getIdManager());
        return ratingDto;
    }

}
