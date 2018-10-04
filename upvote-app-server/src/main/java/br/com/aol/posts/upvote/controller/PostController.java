package br.com.aol.posts.upvote.controller;

import static br.com.aol.posts.upvote.util.AppConstants.*;
import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.aol.posts.upvote.model.Post;
import br.com.aol.posts.upvote.payload.ApiResponse;
import br.com.aol.posts.upvote.payload.PagedResponse;
import br.com.aol.posts.upvote.payload.PostRequest;
import br.com.aol.posts.upvote.payload.PostResponse;
import br.com.aol.posts.upvote.payload.VoteRequest;
import br.com.aol.posts.upvote.security.CurrentUser;
import br.com.aol.posts.upvote.security.UserPrincipal;
import br.com.aol.posts.upvote.service.PostService;

@RestController
@RequestMapping(MAPPING_POSTS)
public class PostController {

	@Autowired
	private PostService postService;

	@GetMapping
	public PagedResponse<PostResponse> getPosts(@CurrentUser final UserPrincipal currentUser,
			@RequestParam(value = PARAM_NAME_PAGE, defaultValue = DEFAULT_PAGE_NUMBER) final int page,
			@RequestParam(value = PARAM_NAME_SIZE, defaultValue = DEFAULT_PAGE_SIZE) final int size) {

		return postService.getAllPosts(currentUser, page, size);
	}

	@PostMapping
	@PreAuthorize(AUTH_ROLE_USER)
	public ResponseEntity<?> createPost(@Valid @RequestBody final PostRequest postRequest) {

		final Post post = postService.createPost(postRequest);
		final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(MAPPING_GET_POST).buildAndExpand(post.getId()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "Post Created Successfully"));
	}

	@GetMapping(MAPPING_GET_POST)
	public PostResponse getPostById(@CurrentUser final UserPrincipal currentUser, @PathVariable final Long postId) {

		return postService.getPostById(postId, currentUser);
	}

	@PostMapping(MAPPING_VOTE)
	@PreAuthorize(AUTH_ROLE_USER)
	public PostResponse castVote(@CurrentUser final UserPrincipal currentUser, @PathVariable final Long postId, @Valid @RequestBody final VoteRequest voteRequest) {

		return postService.castVoteAndGetUpdatedPost(postId, voteRequest, currentUser);
	}

}
