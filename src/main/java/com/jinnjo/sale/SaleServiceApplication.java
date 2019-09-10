package com.jinnjo.sale;

import com.jinnjo.base.config.SpringContextUtil;
import com.jinnjo.base.config.WebMvcConfig;
import com.jinnjo.base.security.CustomUserInfoTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringBootApplication(scanBasePackages = {"com.jinnjo.sale","com.jinnjo.base"})
@EnableFeignClients
@EnableSwagger2
@EnableJpaAuditing
@EnableResourceServer
@Import(value = {WebMvcConfig.class})
public class SaleServiceApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(SaleServiceApplication.class, args);
		SpringContextUtil.setApplicationContext(applicationContext);
	}

	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		return new StringHttpMessageConverter(
				Charset.forName("UTF-8"));
	}

	@Override
	public void configureContentNegotiation(
			ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false);
	}

	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		return corsConfiguration;
	}

	/**
	 * 跨域过滤器
	 *
	 * @return
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig());
		return new CorsFilter(source);
	}

	/**
	 * OAuth 认证鉴权 stringRedisTemplate
	 */
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * cas
	 **/
	@Value("${cas.profile.url}")
	private String profileUrl;

	/**
	 * 初始话资源服务验证 CustomUserInfoTokenService 在 jzcommon.jar
	 */
	@Bean
	public ResourceServerTokenServices resourceServerTokenServices() {
		return new CustomUserInfoTokenService(profileUrl, null, stringRedisTemplate);

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
