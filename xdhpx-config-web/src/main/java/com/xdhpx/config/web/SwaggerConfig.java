package com.xdhpx.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.service.Contact;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
/**
 * @ClassName: SwaggerConfig
 * @Description: Swagger配置类
 * @author 郝瑞龙
*/
@Configuration
@EnableSwagger2
public class SwaggerConfig {
 
	@Value("${swagger.title}")
    private String swaggerTitle;
	@Value("${swagger.description}")  
    private String swaggerDescription;
	
	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) 
                 /**修正Byte转string的Bug**/
                .directModelSubstitute(Byte.class, Integer.class)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))/**扫描所有有api注解的**/
                .paths(PathSelectors.any())
                .build();
    }
 
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerTitle)      /**标题**/
                .description(swaggerDescription)	/**描述**/
                .contact(new Contact("郝瑞龙", null, null))/**联系：作者，网址，邮箱**/
                .termsOfServiceUrl("http://localhost:8080/swagger-ui.html")//
                .version("1.0")/**描述**/
                .build();
    }
 
}
