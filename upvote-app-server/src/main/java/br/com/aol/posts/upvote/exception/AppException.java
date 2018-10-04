package br.com.aol.posts.upvote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Slf4j
public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AppException(final String message, final Throwable cause) {
		super(message, cause);

		log.error(message, cause);
	}

	public AppException(final String message) {
		super(message);

		log.error(message);
	}
}
