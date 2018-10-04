package br.com.aol.posts.upvote.util;

public interface AppConstants {

	final String DEFAULT_PAGE_NUMBER = "0";
	final String DEFAULT_PAGE_SIZE = "30";
	final String PARAM_NAME_PAGE = "page";
	final String PARAM_NAME_SIZE = "size";
	final String AUTH_ROLE_USER = "hasRole('USER')";
	final String MAPPING_GET_POST = "/{postId}";
	final String MAPPING_VOTE = "/{postId}/votes";
	final String MAPPING_POSTS = "/api/posts";

	final int MAX_PAGE_SIZE = 50;
}
