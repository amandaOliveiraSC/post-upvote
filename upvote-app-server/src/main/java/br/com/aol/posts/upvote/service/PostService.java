package br.com.aol.posts.upvote.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.aol.posts.upvote.exception.BadRequestException;
import br.com.aol.posts.upvote.exception.ResourceNotFoundException;
import br.com.aol.posts.upvote.model.Choice;
import br.com.aol.posts.upvote.model.ChoiceVoteCount;
import br.com.aol.posts.upvote.model.Post;
import br.com.aol.posts.upvote.model.User;
import br.com.aol.posts.upvote.model.Vote;
import br.com.aol.posts.upvote.payload.PagedResponse;
import br.com.aol.posts.upvote.payload.PostRequest;
import br.com.aol.posts.upvote.payload.PostResponse;
import br.com.aol.posts.upvote.payload.VoteRequest;
import br.com.aol.posts.upvote.repository.PostRepository;
import br.com.aol.posts.upvote.repository.UserRepository;
import br.com.aol.posts.upvote.repository.VoteRepository;
import br.com.aol.posts.upvote.security.UserPrincipal;
import br.com.aol.posts.upvote.util.AppConstants;
import br.com.aol.posts.upvote.util.ModelMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Autowired
	private UserRepository userRepository;

	public PagedResponse<PostResponse> getAllPosts(final UserPrincipal currentUser, final int page, final int size) {
		validatePageNumberAndSize(page, size);

		// Retrieve Posts
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Post> posts = postRepository.findAll(pageable);

		if (posts.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), posts.getNumber(), posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
		}

		// Map Posts to PostResponses containing vote counts and post creator details
		List<Long> postIds = posts.map(Post::getId).getContent();
		Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(postIds);
		Map<Long, Long> postUserVoteMap = getPostUserVoteMap(currentUser, postIds);
		Map<Long, User> creatorMap = getPostCreatorMap(posts.getContent());

		List<PostResponse> postResponses = posts.map(post -> {
			return ModelMapper.mapPostToPostResponse(post, choiceVoteCountMap, creatorMap.get(post.getCreatedBy()), postUserVoteMap == null ? null : postUserVoteMap.getOrDefault(post.getId(), null));
		}).getContent();

		return new PagedResponse<>(postResponses, posts.getNumber(), posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
	}

	public PagedResponse<PostResponse> getPostsCreatedBy(final String username, final UserPrincipal currentUser, final int page, final int size) {
		validatePageNumberAndSize(page, size);

		User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		// Retrieve all posts created by the given username
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Post> posts = postRepository.findByCreatedBy(user.getId(), pageable);

		if (posts.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), posts.getNumber(), posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
		}

		// Map Posts to PostResponses containing vote counts and post creator details
		List<Long> postIds = posts.map(Post::getId).getContent();
		Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(postIds);
		Map<Long, Long> postUserVoteMap = getPostUserVoteMap(currentUser, postIds);

		List<PostResponse> postResponses = posts.map(post -> {
			return ModelMapper.mapPostToPostResponse(post, choiceVoteCountMap, user, postUserVoteMap == null ? null : postUserVoteMap.getOrDefault(post.getId(), null));
		}).getContent();

		return new PagedResponse<>(postResponses, posts.getNumber(), posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());
	}

	public PagedResponse<PostResponse> getPostsVotedBy(final String username, final UserPrincipal currentUser, final int page, final int size) {
		validatePageNumberAndSize(page, size);

		User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		// Retrieve all postIds in which the given username has voted
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Long> userVotedPostIds = voteRepository.findVotedPostIdsByUserId(user.getId(), pageable);

		if (userVotedPostIds.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), userVotedPostIds.getNumber(), userVotedPostIds.getSize(), userVotedPostIds.getTotalElements(), userVotedPostIds.getTotalPages(),
					userVotedPostIds.isLast());
		}

		// Retrieve all post details from the voted postIds.
		List<Long> postIds = userVotedPostIds.getContent();

		Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
		List<Post> posts = postRepository.findByIdIn(postIds, sort);

		// Map Posts to PostResponses containing vote counts and post creator details
		Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(postIds);
		Map<Long, Long> postUserVoteMap = getPostUserVoteMap(currentUser, postIds);
		Map<Long, User> creatorMap = getPostCreatorMap(posts);

		List<PostResponse> postResponses = posts.stream().map(post -> {
			return ModelMapper.mapPostToPostResponse(post, choiceVoteCountMap, creatorMap.get(post.getCreatedBy()), postUserVoteMap == null ? null : postUserVoteMap.getOrDefault(post.getId(), null));
		}).collect(Collectors.toList());

		return new PagedResponse<>(postResponses, userVotedPostIds.getNumber(), userVotedPostIds.getSize(), userVotedPostIds.getTotalElements(), userVotedPostIds.getTotalPages(),
				userVotedPostIds.isLast());
	}

	public Post createPost(final PostRequest postRequest) {
		Post post = new Post();
		post.setQuestion(postRequest.getQuestion());

		postRequest.getChoices().forEach(choiceRequest -> {
			post.addChoice(new Choice(choiceRequest.getText()));
		});

		Instant now = Instant.now();
		Instant expirationDateTime = now.plus(Duration.ofDays(postRequest.getPostLength().getDays())).plus(Duration.ofHours(postRequest.getPostLength().getHours()));

		post.setExpirationDateTime(expirationDateTime);

		return postRepository.save(post);
	}

	public PostResponse getPostById(final Long postId, final UserPrincipal currentUser) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		// Retrieve Vote Counts of every choice belonging to the current post
		List<ChoiceVoteCount> votes = voteRepository.countByPostIdGroupByChoiceId(postId);

		Map<Long, Long> choiceVotesMap = votes.stream().collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		// Retrieve post creator details
		User creator = userRepository.findById(post.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "id", post.getCreatedBy()));

		// Retrieve vote done by logged in user
		Vote userVote = null;
		if (currentUser != null) {
			userVote = voteRepository.findByUserIdAndPostId(currentUser.getId(), postId);
		}

		return ModelMapper.mapPostToPostResponse(post, choiceVotesMap, creator, userVote != null ? userVote.getChoice().getId() : null);
	}

	public PostResponse castVoteAndGetUpdatedPost(final Long postId, final VoteRequest voteRequest, final UserPrincipal currentUser) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		if (post.getExpirationDateTime().isBefore(Instant.now())) {
			throw new BadRequestException("Sorry! This Post has already expired");
		}

		User user = userRepository.getOne(currentUser.getId());

		Choice selectedChoice = post.getChoices().stream().filter(choice -> choice.getId().equals(voteRequest.getChoiceId())).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Choice", "id", voteRequest.getChoiceId()));

		Vote vote = new Vote();
		vote.setPost(post);
		vote.setUser(user);
		vote.setChoice(selectedChoice);

		try {
			vote = voteRepository.save(vote);
		} catch (DataIntegrityViolationException ex) {
			log.info("User {} has already voted in Post {}", currentUser.getId(), postId);
			throw new BadRequestException("Sorry! You have already cast your vote in this post");
		}

		// -- Vote Saved, Return the updated Post Response now --

		// Retrieve Vote Counts of every choice belonging to the current post
		List<ChoiceVoteCount> votes = voteRepository.countByPostIdGroupByChoiceId(postId);

		Map<Long, Long> choiceVotesMap = votes.stream().collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		// Retrieve post creator details
		User creator = userRepository.findById(post.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "id", post.getCreatedBy()));

		return ModelMapper.mapPostToPostResponse(post, choiceVotesMap, creator, vote.getChoice().getId());
	}

	private void validatePageNumberAndSize(final int page, final int size) {
		if (page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if (size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}

	private Map<Long, Long> getChoiceVoteCountMap(final List<Long> postIds) {
		// Retrieve Vote Counts of every Choice belonging to the given postIds
		List<ChoiceVoteCount> votes = voteRepository.countByPostIdInGroupByChoiceId(postIds);

		Map<Long, Long> choiceVotesMap = votes.stream().collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		return choiceVotesMap;
	}

	private Map<Long, Long> getPostUserVoteMap(final UserPrincipal currentUser, final List<Long> postIds) {
		// Retrieve Votes done by the logged in user to the given postIds
		Map<Long, Long> postUserVoteMap = null;
		if (currentUser != null) {
			List<Vote> userVotes = voteRepository.findByUserIdAndPostIdIn(currentUser.getId(), postIds);

			postUserVoteMap = userVotes.stream().collect(Collectors.toMap(vote -> vote.getPost().getId(), vote -> vote.getChoice().getId()));
		}
		return postUserVoteMap;
	}

	Map<Long, User> getPostCreatorMap(final List<Post> posts) {
		// Get Post Creator details of the given list of posts
		List<Long> creatorIds = posts.stream().map(Post::getCreatedBy).distinct().collect(Collectors.toList());

		List<User> creators = userRepository.findByIdIn(creatorIds);
		Map<Long, User> creatorMap = creators.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		return creatorMap;
	}
}
