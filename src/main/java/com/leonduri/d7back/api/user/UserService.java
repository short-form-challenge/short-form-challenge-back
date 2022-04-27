package com.leonduri.d7back.api.user;

import com.leonduri.d7back.api.user.dto.UserProfileResponseDto;
import com.leonduri.d7back.api.user.dto.UserSignUpRequestDto;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.utils.exception.CEmailSignInFailedException;
import com.leonduri.d7back.utils.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<UserSimpleResponseDto> findAllUser() {
        // {CollectionInstance}.stream().map({method_to_be_mapped})
        // : same function with "map function in python"
        return repository.findAll().stream().map(UserSimpleResponseDto::new).collect(Collectors.toList());
    }

    public UserSimpleResponseDto findUserById(Long userId) throws Exception {
        return new UserSimpleResponseDto(repository.findById(userId)
                .orElseThrow(CUserNotFoundException::new));
    }

    public UserSimpleResponseDto findUserByEmail(String email) throws Exception {
        return new UserSimpleResponseDto(repository.findByEmail(email)
                .orElseThrow(CEmailSignInFailedException::new));
    }

    public UserSimpleResponseDto save(UserSignUpRequestDto requestDto) {
        return new UserSimpleResponseDto(repository.save(requestDto.toEntity()));
    }

    public UserProfileResponseDto getUserProfile(Long userId) throws Exception {
        return new UserProfileResponseDto(repository.findById(userId)
                .orElseThrow(CUserNotFoundException::new));
    }
}
