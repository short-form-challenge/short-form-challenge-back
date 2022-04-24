package com.leonduri.d7back.api.video;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    @Override
    List<Video> findAll();

    @Override
    Optional<Video> findById(Long aLong);
}
