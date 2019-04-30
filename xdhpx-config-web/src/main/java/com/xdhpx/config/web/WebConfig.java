package com.xdhpx.config.web;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * @ClassName: WebConfig
 * @Description: SpringBoot配置类WEB相关
 * 	采用JavaBean的形式来代替传统的xml配置文件形式进行针对框架个性化定制
 * @author  郝瑞龙
*/
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	
    /**
     * 	消息转化器
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    	/**声明fastjson消息转化器**/
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        
        /**声明默认配置规则**/
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue);
        
        /**配置规则添加到转换器中**/
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        
        /**把自定义的替换SpringBoot默认的**/
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        converters.add(fastJsonHttpMessageConverter);
    }
    
    /**
     * 	资源匹配设置,值为false表示favorPathExtension表示支持后缀匹配
     */
  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
      configurer.favorPathExtension(false);
  }
    
    /**
     * 	跨域设置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")/**设置允许跨域的路径**/
                .allowedOrigins("*")/**设置允许跨域请求的域名**/
                .allowCredentials(true)/**是否允许证书 不再默认开启**/
                .allowedMethods("*")/**设置允许的方法,可指定方法allowedMethods("GET", "POST", "PUT", "DELETE")**/
                .maxAge(3600);/**跨域允许时间**/
    }

}