package com.leonduri.d7back.api.challenge;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"4. Challenge"})
@RestController
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeRepository challengeRepository;

    @ApiOperation(value = "챌린지 조회", notes = "모든 챌린지를 조회한다.")
    @GetMapping(value = "/challenges")
    public List<Challenge> challengeList() {
        return challengeRepository.findAll();
    }
}
