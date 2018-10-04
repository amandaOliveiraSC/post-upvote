package br.com.aol.posts.upvote;

import java.time.ZoneId;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackageClasses = { PostsApplication.class, Jsr310JpaConverters.class })
public class PostsApplication {

	@PostConstruct
	void init() {
//		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.systemDefault()));
	}

	public static void main(String[] args) {
		SpringApplication.run(PostsApplication.class, args);
	}
}
