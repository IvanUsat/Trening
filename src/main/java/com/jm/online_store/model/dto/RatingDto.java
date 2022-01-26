package com.jm.online_store.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RatingDto {
    private Long idManager;
//    private Long idCustomer;
    private List<Integer> score;
    private Double rating;

}
