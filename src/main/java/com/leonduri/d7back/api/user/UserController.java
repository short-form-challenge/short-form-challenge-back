package com.leonduri.d7back.api.user;

import com.leonduri.d7back.api.user.dto.UserSimpleResponseDto;
import com.leonduri.d7back.utils.ListApiResponse;
import com.leonduri.d7back.utils.SingleApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "전체 유저 조회", notes = "모든 유저를 조회한다.") // 각각의 resource에 제목과 설명 표시
    @GetMapping(value = "/users")
    public ListApiResponse<UserSimpleResponseDto> findAllUser() {
        return ListApiResponse.success(service.findAllUser());
    }

    @ApiOperation(value = "특정 유저 조회", notes = "userId로 유저 한 명을 조회한다.")
    @GetMapping(value = "/users/{userId}")
    public SingleApiResponse<UserSimpleResponseDto> findUserById(
            @ApiParam(value = "유저 id", required = true) @PathVariable long userId
    ) throws Exception {
        return SingleApiResponse.success(service.findUserById(userId));
    }

}