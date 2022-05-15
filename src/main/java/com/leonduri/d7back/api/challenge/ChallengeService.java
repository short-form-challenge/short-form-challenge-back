package com.leonduri.d7back.api.challenge;

import com.leonduri.d7back.api.challenge.dto.ChallengeSimpleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    public List<ChallengeSimpleResponseDto> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        return challenges.stream().map(ChallengeSimpleResponseDto::new).collect(Collectors.toList());
    }
}
