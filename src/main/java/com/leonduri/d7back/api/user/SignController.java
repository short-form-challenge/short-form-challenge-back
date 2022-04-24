package com.leonduri.d7back.api.user;

import com.leonduri.d7back.api.user.dto.JwtResponseDto;
import com.leonduri.d7back.api.user.dto.UserSignUpRequestDto;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.config.security.JwtTokenProvider;
import com.leonduri.d7back.utils.SingleApiResponse;
import com.leonduri.d7back.utils.exception.CEmailSignInFailedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"0. SignIn/SignUp"})
@RequiredArgsConstructor
@RestController
//@RequestMapping(value = "/v1")
public class SignController {

    private final UserRepository repository;
    private final UserService service;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일로 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleApiResponse<JwtResponseDto> signIn(
            @ApiParam(value = "이메일", required = true) @RequestParam String email,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
        User user = repository.findByEmail(email).orElseThrow(CEmailSignInFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CEmailSignInFailedException();
        }

        // TODO
        // login 시 마다, challenge 정보 재확인 처리 필요
        // 만약 해당 유저의 챌린지 중 해당 챌린지의 lastChallengedAt이 로그인 한 날짜와 2일 이상 차이나는 챌린지가 있다면
        // 해당 챌린지에 대하여 dayCnt를 0으로 처리해야 함

        return SingleApiResponse.success(new JwtResponseDto(
                user.getId(),
                jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles())
        ));
    }

    @ApiOperation(value = "회원 가입", notes = "회원 가입을 한다.")
    @PostMapping(value = "/signup")
    public SingleApiResponse<UserSimpleResponseDto> signUp(
            @ApiParam(value = "이메일", required = true) @RequestParam String email,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
            @ApiParam(value = "닉네임", required = true) @RequestParam String nickname) {
        return SingleApiResponse.success(service.save(
                new UserSignUpRequestDto(email, passwordEncoder.encode(password), nickname)
        ));
    }
}