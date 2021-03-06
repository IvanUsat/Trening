package com.jm.online_store.repository;

import com.jm.online_store.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    boolean existsByTopicName(String topicName);

    List<Topic> findTopicByTopicsCategoryId(long id);

    Topic findByTopicName(String topicName);

    Topic findById(long id);
}
