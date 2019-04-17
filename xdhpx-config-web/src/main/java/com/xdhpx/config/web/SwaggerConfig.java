package com.xdhpx.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
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
@Profile({"local","dev","test"})/**在本地环境、开发环境、测试环境开启**/
public class SwaggerConfig {
 
	@Value("${swagger.title:docTitle}")
    private String swaggerTitle;
	@Value("${swagger.description:docInfo}")  
    private String swaggerDescription;
	@Value("${swagger.author:HRL}")  
    private String swaggerAuthor;
	
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
                .contact(new Contact(swaggerAuthor, null, null))/**联系：作者，网址，邮箱**/
                .termsOfServiceUrl("http://localhost:8080/swagger-ui.html")//
                .version("1.0")/**描述**/
                .build();
    }
 
}
