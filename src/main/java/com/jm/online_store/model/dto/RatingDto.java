package com.jm.online_store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RatingDto {
    private Long idManager;
//    private Long idCustomer;
    private List<Integer> score;
    private Double rating;

}
