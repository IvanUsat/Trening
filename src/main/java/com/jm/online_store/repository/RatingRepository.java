package com.jm.online_store.repository;

import com.jm.online_store.model.Rating;
import com.jm.online_store.model.dto.RatingDto;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.UniqueConstraint;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {


    @Modifying
    @Query(nativeQuery = true, value = "INSERT  INTO rating as r (rating, user_id) VALUES ( ?1, ?2) ON CONFLICT (user_id) DO UPDATE SET rating = ?1")
    void saveByUserId(Double rating, Long userId);

    Rating findAllByUserId(Long userid);
}
