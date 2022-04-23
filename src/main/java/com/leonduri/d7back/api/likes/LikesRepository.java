package com.leonduri.d7back.api.likes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Override
    List<Likes> findAll();
}
