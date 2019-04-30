package com.xdhpx.config.redis;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching/**启用缓存**/
@EnableConfigurationProperties(RedisProperties.class) /**这个会把配置文件属性，映射到bean类，只要属性一样**/
public class RedisConfig extends CachingConfigurerSupport{
	
	@Autowired
    private RedisProperties redisProperties;
	
	@Autowired
    private JedisConnectionFactory jedisConnectionFactory;
    
    /**  
     	写在前面的话
     	Spring Boot会在侦测到存在Redis的依赖并且Redis的配置是可用的情况下，
     	使用RedisCacheManager初始化CacheManager。
     	也就是说要使用缓存的话，SpringBoot就会选择Redis来作为缓存的容器。
     **/
	
	/**
     * 生成key的策略
     *
     * @return
     */
	@Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }
	
	/**缓存管理**/
  	@Bean
    @Override
    public CacheManager cacheManager() {
  		
        /**
         初始化缓存管理器
         在这里我们可以这是全局的参数,比如缓存的整体过期时间什么的
         我这里默认没有配置
        **/
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(jedisConnectionFactory);
        return builder.build();
    }
  	
    /** 
     * RedisTemplate配置
     * 
     * redis模板操作类,类似于jdbcTemplate的一个类;
     *
     * 虽然CacheManager也能获取到Cache对象，但是操作起来没有那么灵活；
     *
     * 这里在扩展下：RedisTemplate这个类不见得很好操作，我们可以在进行扩展一个我们
     *
     * 自己的缓存类，比如：RedisStorage类;
     *
     * @param factory : 通过Spring进行注入，参数在application.properties进行配置；
     * @return
     */
    @Bean
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        
        /**设置序列化工具，这样ReportBean不需要实现Serializable接口**/
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
       
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        
        jackson2JsonRedisSerializer.setObjectMapper(om);
        
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    
    /**
     *  自定义定义redisConnectionFactory
     */
	@Bean
	 public JedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration ();
		redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
		
        
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(redisProperties.getTimeout());
		
        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,jedisClientConfiguration.build());
		  return factory;
	}
	    

}