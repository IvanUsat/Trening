package com.jm.online_store.controller.rest;

import com.jm.online_store.model.dto.OrderDTO;
import com.jm.online_store.model.dto.RatingDto;
import com.jm.online_store.service.interf.RatingService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class RatingGetRestController {

    private final RatingService ratingService;

    @GetMapping("/getScore")
    public ResponseEntity<RatingDto> getScoreById(@RequestBody Long idManager) {
        RatingDto ratingDto = ratingService.findRatingDtoById(idManager);
        return ResponseEntity.ok(ratingDto);
    }

    @GetMapping("/getScore1")
    public ResponseEntity<RatingDto> getScoreById() {
RatingDto ratingDto = new RatingDto();
List<Integer> integerList =List.of(1, 2, 3, 4, 5);
ratingDto.setScore(integerList);
ratingDto.setIdManager(1L);
        return ResponseEntity.ok(ratingDto);
    }

}
