package com.leonduri.d7back.api.user;

import com.leonduri.d7back.api.user.dto.*;
import com.leonduri.d7back.config.security.JwtTokenProvider;
import com.leonduri.d7back.utils.ApiResponse;
//import com.leonduri.d7back.utils.RedisService;
import com.leonduri.d7back.utils.SingleApiResponse;
import com.leonduri.d7back.utils.exception.*;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.net.http.HttpRequest;

@Api(tags = {"0. SignIn/SignUp"})
@RequiredArgsConstructor
@RestController
//@RequestMapping(value = "/v1")
public class SignController {

    private final UserRepository userRepository;

    private final UserService userService;
//    private final RedisService redisService;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    @ApiOperation(value = "로그인", notes = "이메일로 로그인을 한다.")
    @PostMapping(value = "/signin")
    public ApiResponse signIn(
            HttpServletResponse response,
            @RequestBody @ApiParam(value = "유저 로그인 정보", required = true) UserSignInRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(CEmailSignInFailedException::new);
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CEmailSignInFailedException();
        }

        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles());
//        String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(user.getId()), user.getRoles());
        response.setHeader("X-AUTH-TOKEN", accessToken);
//        response.setHeader("REFRESH-TOKEN", refreshToken);

//        redisService.set(refreshToken, user.getEmail());
        return ApiResponse.success("로그인에 성공했습니다. 헤더에 담긴 토큰을 확인하세요.");
    }

    @ApiOperation(value = "회원 가입", notes = "회원 가입을 한다.")
    @PostMapping(value = "/signup")
    public SingleApiResponse<UserSimpleResponseDto> signUp(
            @RequestBody @ApiParam(value = "회원 가입에 필요한 유저 정보", required = true)
                    UserSignUpRequestDto requestDto) {
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return SingleApiResponse.success(userService.save(requestDto));
    }

    @ApiOperation(value = "이메일 중복 검사", notes = "이메일 중복을 검사한다.")
    @GetMapping(value = "/validate/email")
    public ApiResponse validateEmail(
            @ApiParam(required = true) @PathParam(value = "email") String email) {
        if (userService.validateEmail(email)) return ApiResponse.success("이메일 형식에 맞다면 사용 가능한 이메일입니다.");
        throw new CEmailInvalidException();
    }

    @ApiOperation(value = "닉네임 중복 검사", notes = "닉네임 중복을 검사한다.")
    @GetMapping(value = "/validate/nickname")
    public ApiResponse validateNickname(
            @ApiParam(required = true) @PathParam(value = "nickname") String nickname) {
        if (userService.validateNickname(nickname)) return ApiResponse.success("닉네임 형식에 맞다면 사용 가능한 닉네임입니다.");
        throw new CNicknameInvalidException();
    }

    // TODO spring security 확인
//    @ApiOperation(value = "로그아웃", notes = "로그아웃을 하고 refresh token을 무효화한다.")
//    @GetMapping(value = "/logout")
//    public ApiResponse logout(HttpServletRequest request) {
//        redisService.delete(request.getHeader("REFRESH-TOKEN"));
//        return ApiResponse.success("로그아웃 되었습니다.");
//    }

//    @ApiOperation(value = "access token 재발급", notes = "refresh token을 이용해 이메일 중복을 검사한다.")
//    @PostMapping(value = "/access_token")
//    public ApiResponse getAccessToken(HttpServletRequest request, HttpServletResponse response) {
//        String refreshToken = request.getHeader("REFRESH-TOKEN");
//        if (redisService.get(refreshToken) == null || !jwtTokenProvider.validateToken(refreshToken))
//            throw new CInvalidJwtTokenException();
//        User user = userRepository.findById(Long.parseLong(jwtTokenProvider.getUserPk(refreshToken)))
//                .orElseThrow(CUserNotFoundException::new);
//        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles());
//        response.setHeader("X-AUTH-TOKEN", accessToken);
//        return ApiResponse.success("access token이 재발급 되었습니다.");
//    }



}