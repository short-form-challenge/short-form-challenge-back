package com.leonduri.d7back.api.User;

import com.leonduri.d7back.api.User.dto.JwtResponseDto;
import com.leonduri.d7back.api.User.dto.UserSignUpRequestDto;
import com.leonduri.d7back.api.User.dto.UserSimpleResponseDto;
import com.leonduri.d7back.config.security.JwtTokenProvider;
import com.leonduri.d7back.utils.ApiResponse;
import com.leonduri.d7back.utils.ListApiResponse;
import com.leonduri.d7back.utils.SingleApiResponse;
import com.leonduri.d7back.utils.exception.CEmailSignInFailedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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