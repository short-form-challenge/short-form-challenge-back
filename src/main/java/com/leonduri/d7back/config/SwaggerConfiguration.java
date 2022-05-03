package com.leonduri.d7back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

//@Configuration
//@EnableSwagger2
//public class SwaggerConfiguration {
//
//    @Bean
//    public Docket swaggerApi() {
//        return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
//                .apis(RequestHandlerSelectors.basePackage("com.leonduri.d7back.api"))
//                // basePackage : controller 하단의 Controller 내용을 읽어 mapping된 resource들을 문서화 시킴
//                .paths(PathSelectors.any())
//                .build();
////                .useDefaultResponseMessages(false); // 기본으로 세팅되는 200,401,403,404 메시지 표시 x
//    }
//
//    private ApiInfo swaggerInfo() {
//        return new ApiInfoBuilder()
//                .title("Spring API Documentation")
//                .description("앱 개발시 사용되는 서버 API에 대한 연동 문서입니다.")
////                .license("pepega").licenseUrl("https://blog.naver.com/gudrb963")
//                .version("1")
//                .build();
//    }
//
//    private Set<String> getConsumeContentTypes() {
//        Set<String> consumes = new HashSet<>();
//        consumes.add("application/json;charset=UTF-8");
//        consumes.add("application/x-www-form-urlencoded");
//        consumes.add("multipart/form-data");
//        return consumes;
//    }
//
//    private Set<String> getProduceContentTypes() {
//        Set<String> produces = new HashSet<>();
//        produces.add("application/json;charset=UTF-8");
//        produces.add("multipart/form-data");
//        return produces;
//    }
//}

public class SwaggerConfiguration implements WebMvcConfigurer {
    private final String baseUrl;

    public SwaggerConfiguration(String baseUrl) {
        this.baseUrl = "";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
        registry.
                addResourceHandler(baseUrl)
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(baseUrl)
                .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
    }
}