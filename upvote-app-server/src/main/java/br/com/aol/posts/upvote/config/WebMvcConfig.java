package br.com.aol.posts.upvote.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private static final String METHOD_DELETE = "DELETE";
	private static final String METHOD_PATCH = "PATCH";
	private static final String METHOD_PUT = "PUT";
	private static final String METHOD_POST = "POST";
	private static final String METHOD_GET = "GET";
	private static final String METHOD_OPTIONS = "OPTIONS";
	private static final String METHOD_HEAD = "HEAD";

	private static final String ORIGIN_ALL = "*";

	private static final String PATTERN_ALL = "/**";

	private static final long MAX_AGE_SECS = 3600;

	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		
		registry.addMapping(PATTERN_ALL)
		.allowedOrigins(ORIGIN_ALL)
		.allowedMethods(METHOD_HEAD, METHOD_OPTIONS,METHOD_GET, METHOD_POST, METHOD_PUT, METHOD_PATCH, METHOD_DELETE)
		.maxAge(MAX_AGE_SECS);
	}
}
