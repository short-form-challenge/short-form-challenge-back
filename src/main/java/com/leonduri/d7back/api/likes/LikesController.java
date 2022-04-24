package com.leonduri.d7back.api.likes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"3. Likes"})
@RequiredArgsConstructor
@RestController
public class LikesController {
    private final LikesRepository likesRepository;

    @ApiOperation(value = "좋아요 조회", notes = "모든 좋아요를 조회한다.")
    @GetMapping(value = "/likes")
    public List<Likes> likesList() {
        return likesRepository.findAll();
    }

}
