package com.leonduri.d7back.api.user;

import com.leonduri.d7back.api.user.dto.*;
import com.leonduri.d7back.config.security.JwtTokenProvider;
import com.leonduri.d7back.utils.SingleApiResponse;
import com.leonduri.d7back.utils.exception.CEmailSignInFailedException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
            @RequestBody @ApiParam(value = "유저 로그인 정보", required = true) UserSignInRequestDto requestDto) {
        User user = repository.findByEmail(requestDto.getEmail()).orElseThrow(CEmailSignInFailedException::new);
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CEmailSignInFailedException();
        }

        // TODO refreshToken
        return SingleApiResponse.success(new JwtResponseDto(
                user.getId(),
                jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles())
        ));
    }

    @ApiOperation(value = "회원 가입", notes = "회원 가입을 한다.")
    @PostMapping(value = "/signup")
    public SingleApiResponse<UserSimpleResponseDto> signUp(
            @RequestBody @ApiParam(value = "회원 가입에 필요한 유저 정보", required = true)
                    UserSignUpRequestDto requestDto) {
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return SingleApiResponse.success(service.save(requestDto));
    }
}