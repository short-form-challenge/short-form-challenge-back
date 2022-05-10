package com.leonduri.d7back.api.user;

import com.leonduri.d7back.api.challenge.Challenge;
import com.leonduri.d7back.api.challenge.ChallengeRepository;
import com.leonduri.d7back.api.user.dto.*;
import com.leonduri.d7back.api.video.dto.AdminMainVideoListResponseDto;
import com.leonduri.d7back.utils.FileSystemStorageService;
import com.leonduri.d7back.utils.exception.CEmailSignInFailedException;
import com.leonduri.d7back.utils.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final FileSystemStorageService storageService;

    Long count = 3L;

    public List<UserSimpleResponseDto> findAllUser() {
        // {CollectionInstance}.stream().map({method_to_be_mapped})
        // : same function with "map function in python"
        return userRepository.findAll().stream().map(UserSimpleResponseDto::new).collect(Collectors.toList());
    }

    public UserSimpleResponseDto findUserById(Long userId) throws Exception {
        return new UserSimpleResponseDto(userRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new));
    }

    public UserSimpleResponseDto findUserByEmail(String email) throws Exception {
        return new UserSimpleResponseDto(userRepository.findByEmail(email)
                .orElseThrow(CEmailSignInFailedException::new));
    }

    public UserSimpleResponseDto save(UserSignUpRequestDto requestDto) {
        return new UserSimpleResponseDto(userRepository.save(requestDto.toEntity()));
    }

    public UserProfileResponseDto getUserProfile(Long userId) throws Exception {
         User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);

         // challenge update
        List<Challenge> challenges = user.getChallenges();
        for (Challenge c: challenges) {
            c.setBadgeCnt(c.getBadgeCnt() + c.getDayCnt() / 7);
            c.setDayCnt(c.getDayCnt() % 7);
            // challenge failed
            if (!LocalDate.now().minusDays(1L).isAfter(c.getLastChallengedAt())) c.setDayCntZero();
            challengeRepository.save(c);
        }
        return new UserProfileResponseDto(user);
    }

    public UserUpdateResponseDto updateUser(long userId, MultipartFile profileFile, String nickname) {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        if (profileFile != null) user.setProfileFilePath(storageService.storeProfile(profileFile, user.getEmail()));
        if (nickname != null) user.setNickname(nickname);
        userRepository.save(user);
        return new UserUpdateResponseDto(user);
    }


  //    Admin API
    public List<AdminUserResponseDto> findAdminUserList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepo.findAll(pageRequest).stream().map(AdminUserResponseDto::new).collect(Collectors.toList());
    }

    public Long getCountAllUsers() {
        return userRepo.countAllUser();
    }
  
    public boolean validateEmail(String email) {
        User u = userRepository.findByEmail(email).orElse(new User(-1L));
        return u.getId() == -1L;
    }

    public boolean validateNickname(String nickname) {
        User u = userRepository.findByNickname(nickname).orElse(new User(-1L));
        return u.getId() == -1L;
    }
}
