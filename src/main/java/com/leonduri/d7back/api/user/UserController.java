package com.leonduri.d7back.api.user;

import com.leonduri.d7back.api.user.dto.UserProfileResponseDto;
import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.api.user.dto.UserUpdateResponseDto;
import com.leonduri.d7back.config.security.JwtTokenProvider;
import com.leonduri.d7back.utils.ListApiResponse;
import com.leonduri.d7back.utils.SingleApiResponse;
import com.leonduri.d7back.utils.exception.CInvalidJwtTokenException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"1. User"})
@RequiredArgsConstructor
@RestController
//@RequestMapping(value = "/v1")
public class UserController {

//    @RequestMapping(value="/", method= RequestMethod.GET)
//    public String mainPage() {
//        return "redirect:/user";
//    }
    private final UserService service;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "전체 유저 조회", notes = "모든 유저를 조회한다.") // 각각의 resource에 제목과 설명 표시
    @GetMapping(value = "/users")
    public ListApiResponse<UserSimpleResponseDto> getAllUsers() {
        return ListApiResponse.success(service.findAllUser());
    }

    @ApiOperation(value = "특정 유저 조회", notes = "userId로 유저 한 명을 조회한다.")
    @GetMapping(value = "/users/{userId}")
    public SingleApiResponse<UserSimpleResponseDto> getUserById(
            @ApiParam(value = "유저 id", required = true) @PathVariable long userId
    ) throws Exception {
        return SingleApiResponse.success(service.findUserById(userId));
    }


    @ApiOperation(value = "특정 유저의 프로필 조회", notes = "userId로 유저 한 명의 프로필을 조회한다.")
    @GetMapping(value = "/users/{userId}/profile")
    public SingleApiResponse<UserProfileResponseDto> getUserProfile(
            @ApiParam(value = "유저 id", required = true) @PathVariable long userId
    ) throws Exception {
        return SingleApiResponse.success(service.getUserProfile(userId));
    }

    @Secured("ROLE_USER")
    @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token",
                    required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "유저 본인의 프로필 수정", notes = "로그인 한 유저의 프로필을 수정한다.",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PutMapping(value = "/users/profile/")
    public SingleApiResponse<UserUpdateResponseDto> updateUser(
            HttpServletRequest request,
            @ApiParam(value = "프로필 사진", required = false)
            @RequestPart(value = "profileFile", required = false)
                    MultipartFile profileFile,
            @ApiParam(value = "수정할 닉네임", required = false) @RequestParam String nickname) throws Exception{
        String jwt = jwtTokenProvider.resolveToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();
        return SingleApiResponse.success(service.updateUser(
                Long.valueOf(jwtTokenProvider.getUserPk(jwt)), profileFile, nickname)
        );
    }

}