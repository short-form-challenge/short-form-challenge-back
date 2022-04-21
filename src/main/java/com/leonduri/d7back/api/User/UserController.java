package com.leonduri.d7back.api.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"1. User"})
@RequiredArgsConstructor
@RestController
//@RequestMapping(value = "/v1")
public class UserController {
    private final UserRepository repo;

//    @RequestMapping(value="/", method= RequestMethod.GET)
//    public String mainPage() {
//        return "redirect:/user";
//    }

    @ApiOperation(value = "회원 조회", notes = "모든 회원을 조회한다.") // 각각의 resource에 제목과 설명 표시
    @GetMapping(value = "/users") // user 테이블의 모든 정보를 읽어옴
    public List<User> userlist() { // 데이터가 1개 이상일 수 있기에 List<User>로 선언
        return repo.findAll(); // JPA를 사용하면 CRUD에 대해 설정 없이 쿼리 사용 가능 (select * from user 와 같음)
    }

}