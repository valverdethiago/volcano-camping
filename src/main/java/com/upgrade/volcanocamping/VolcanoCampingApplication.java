package com.upgrade.volcanocamping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;

/**
 * @author valverde.thiago
 */
@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
@EnableSwagger2
public class VolcanoCampingApplication {

    public static void main(String[] args) {
        SpringApplication.run(VolcanoCampingApplication.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.upgrade.volcanocamping.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData())
                .directModelSubstitute(LocalDate.class, String.class);
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Volcano Camping REST API")
                .description("\"REST API for reservations management\"")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                .build();
    }

}
