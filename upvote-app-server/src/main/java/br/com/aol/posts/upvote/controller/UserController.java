package br.com.aol.posts.upvote.controller;

import static br.com.aol.posts.upvote.util.AppConstants.DEFAULT_PAGE_NUMBER;
import static br.com.aol.posts.upvote.util.AppConstants.DEFAULT_PAGE_SIZE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.aol.posts.upvote.exception.ResourceNotFoundException;
import br.com.aol.posts.upvote.model.User;
import br.com.aol.posts.upvote.payload.PagedResponse;
import br.com.aol.posts.upvote.payload.PostResponse;
import br.com.aol.posts.upvote.payload.UserIdentityAvailability;
import br.com.aol.posts.upvote.payload.UserProfile;
import br.com.aol.posts.upvote.payload.UserSummary;
import br.com.aol.posts.upvote.repository.PostRepository;
import br.com.aol.posts.upvote.repository.UserRepository;
import br.com.aol.posts.upvote.repository.VoteRepository;
import br.com.aol.posts.upvote.security.CurrentUser;
import br.com.aol.posts.upvote.security.UserPrincipal;
import br.com.aol.posts.upvote.service.PostService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Autowired
	private PostService postService;

	@GetMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public UserSummary getCurrentUser(@CurrentUser final UserPrincipal currentUser) {
		
		return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
	}

	@GetMapping("/user/checkUsernameAvailability")
	public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") final String username) {
		
		return new UserIdentityAvailability(!userRepository.existsByUsername(username));
	}

	@GetMapping("/user/checkEmailAvailability")
	public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") final String email) {
		
		return new UserIdentityAvailability(!userRepository.existsByEmail(email));
	}

	@GetMapping("/users/{username}")
	public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
		
		final User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
		final long postCount = postRepository.countByCreatedBy(user.getId());
		final long voteCount = voteRepository.countByUserId(user.getId());

		UserProfile userProfile = new UserProfile(user, postCount, voteCount);

		log.info("Profile id=[%d] found (username=[%s]).", userProfile.getId(), username);
		return userProfile;
	}

	@GetMapping("/users/{username}/posts")
	public PagedResponse<PostResponse> getPostsCreatedBy(
			@PathVariable(value = "username") final String username, 
			@CurrentUser final UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) final int page, 
			@RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) final int size) {
		
		return postService.getPostsCreatedBy(username, currentUser, page, size);
	}

	@GetMapping("/users/{username}/votes")
	public PagedResponse<PostResponse> getPostsVotedBy(
			@PathVariable(value = "username") final String username, 
			@CurrentUser final UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) final int page, 
			@RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) final int size) {
		
		return postService.getPostsVotedBy(username, currentUser, page, size);
	}

}
