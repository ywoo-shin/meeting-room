package com.pay.room.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;
import java.util.Locale;

@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    private static final String BASE_PACKAGE = "com.pay.room.web";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    private Docket api(String versionName, String versionPath) {
        String title = "회의실 예약 API";
        return getInstanceDocket().apiInfo(getApiInfo(title, versionName))
                                  .groupName(title + " (" + versionName + ")")
                                  .select()
                                  .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                                  .paths(PathSelectors.ant("/pay/" + versionPath + "/**"))
                                  .build();
    }

    private Docket getInstanceDocket() {
        return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
                                                      .directModelSubstitute(Locale.class, String.class)
                                                      .directModelSubstitute(Date.class, String.class)
                                                      .genericModelSubstitutes(Mono.class)
                                                      .genericModelSubstitutes(Flux.class);
    }

    private ApiInfo getApiInfo(String title, String version) {
        return new ApiInfoBuilder()
            .title(title)
            .description("")
            .version(version)
            .build();
    }

    // ========== ADMIN ==========
    @Bean
    public Docket apis_V_1() {
        return api("v1", "v1");
    }
}
