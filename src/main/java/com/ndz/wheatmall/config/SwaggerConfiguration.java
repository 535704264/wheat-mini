package com.ndz.wheatmall.config;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.ndz.wheatmall.common.bean.ApiResult;
import com.ndz.wheatmall.vo.index.GreetVO;
import com.ndz.wheatmall.vo.sys.LogErrorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Map;

import static springfox.documentation.schema.AlternateTypeRules.newRule;


@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfiguration {

    @Bean
    public Docket defaultApi2() {
        TypeResolver typeResolver = new TypeResolver();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
            // 配置文档信息
            .apiInfo(apiInfo("# 麦子商城APIs", "1.0"))
            // 配置是否启用Swagger，如果是false，在浏览器将无法访问
            .enable(true)
            // 扫描所有，项目中的所有接口都会被扫描到
            .select()
            // 这里指定Controller扫描包路径
            .apis(RequestHandlerSelectors.basePackage("com.ndz.wheatmall"))
            // 配置接口扫描过滤
            .paths(PathSelectors.ant("/**")).build();
        return docket;
    }

    private ApiInfo apiInfo(String description, String version) {
        ApiInfo apiInfo = new ApiInfoBuilder().contact("Jack Lee")
                .description(description).version(version)
            .termsOfServiceUrl("http://www.wheatmall.com").build()
                ;
        return apiInfo;
    }
}
