package com.leonduri.d7back.api.challenge;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    @Override
    List<Challenge> findAll(Sort sort);
}
