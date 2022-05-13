package com.leonduri.d7back.api.user;

import com.leonduri.d7back.api.user.dto.AdminUserResponseDto;
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
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "accessToken",
                    required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "유저 본인의 프로필 수정", notes = "로그인 한 유저의 프로필을 수정한다.",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PutMapping(value = "/users/profile")
    public SingleApiResponse<UserUpdateResponseDto> updateUser(
            HttpServletRequest request,
            @ApiParam(value = "프로필 사진", required = false)
            @RequestPart(value = "profileFile", required = false)
                    MultipartFile profileFile,
            @ApiParam(value = "수정할 닉네임", required = false) @RequestParam String nickname) {
        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();
        return SingleApiResponse.success(service.updateUser(
                Long.parseLong(jwtTokenProvider.getUserPk(jwt)), profileFile, nickname)
        );
    }

    @ApiOperation(value = "Admin 페이지 유저의 리스트 조회", notes = "Admin 페이지에서 모든 유저를 조회한다.")
    @GetMapping(value = "/admin/users/{page}")
    public ListApiResponse<AdminUserResponseDto> getAdminUserList(@PathVariable Integer page) {
        Long countUser = service.getCountAllUsers();
        return ListApiResponse.adminSuccess(service.findAdminUserList(page, 10), countUser);
    }

    @Secured("ROLE_USER")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "accessToken",
            required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "유저 본인의 프로필 조회", notes = "로그인 한 유저의 프로필을 조회한다.")
    @GetMapping(value = "/users/myProfile")
    public SingleApiResponse<UserProfileResponseDto> getMyProfile(
            HttpServletRequest request
    ) throws Exception {
        String jwt = jwtTokenProvider.resolveAccessToken(request);
        if (!jwtTokenProvider.validateToken(jwt)) throw new CInvalidJwtTokenException();
        return SingleApiResponse.success(service.getUserProfile(Long.parseLong(jwtTokenProvider.getUserPk(jwt))));
    }

}
