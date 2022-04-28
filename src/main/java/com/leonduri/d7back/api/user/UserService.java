package com.leonduri.d7back.api.user;

import com.leonduri.d7back.api.challenge.Challenge;
import com.leonduri.d7back.api.challenge.ChallengeRepository;
import com.leonduri.d7back.api.user.dto.UserProfileResponseDto;
import com.leonduri.d7back.api.user.dto.UserSignUpRequestDto;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.utils.exception.CEmailSignInFailedException;
import com.leonduri.d7back.utils.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final ChallengeRepository challengeRepo;


    public List<UserSimpleResponseDto> findAllUser() {
        // {CollectionInstance}.stream().map({method_to_be_mapped})
        // : same function with "map function in python"
        return userRepo.findAll().stream().map(UserSimpleResponseDto::new).collect(Collectors.toList());
    }

    public UserSimpleResponseDto findUserById(Long userId) throws Exception {
        return new UserSimpleResponseDto(userRepo.findById(userId)
                .orElseThrow(CUserNotFoundException::new));
    }

    public UserSimpleResponseDto findUserByEmail(String email) throws Exception {
        return new UserSimpleResponseDto(userRepo.findByEmail(email)
                .orElseThrow(CEmailSignInFailedException::new));
    }

    public UserSimpleResponseDto save(UserSignUpRequestDto requestDto) {
        return new UserSimpleResponseDto(userRepo.save(requestDto.toEntity()));
    }

    public UserProfileResponseDto getUserProfile(Long userId) throws Exception {
         User user = userRepo.findById(userId).orElseThrow(CUserNotFoundException::new);

         // challenge update
        List<Challenge> challenges = user.getChallenges();
        for (Challenge c: challenges) {
            c.setBadgeCnt(c.getDayCnt() / 7);
            c.setDayCnt(c.getDayCnt() % 7);
            // challenge failed
            if (!LocalDate.now().minusDays(1L).isAfter(c.getLastChallengedAt())) c.setDayCntZero();
            challengeRepo.save(c);
        }
        return new UserProfileResponseDto(user);
    }
}
