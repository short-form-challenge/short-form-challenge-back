package com.leonduri.d7back.api.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"5. Category"})
@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @ApiOperation(value = "카테고리 조회", notes = "모든 카테고리르 조회한다")
    @GetMapping(value = "/categories")
    public List<Category> categoryList() {
        return categoryRepository.findAll();
    }
}
