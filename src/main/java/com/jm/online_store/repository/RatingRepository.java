package com.jm.online_store.repository;

import com.jm.online_store.model.Rating;
import com.jm.online_store.model.dto.RatingDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.UniqueConstraint;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {


    @Query("UPDATE Rating r set r.rating =: ratingDto WHERE r.user.id = ?1")
    Rating updateOrSave(@Param("ratingDto") RatingDto ratingDto);

    Rating findAllByUserId(Long userid);
}
