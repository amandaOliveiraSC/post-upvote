package br.com.aol.posts.upvote.util;

import java.util.Map;

import br.com.aol.posts.upvote.model.Post;
import br.com.aol.posts.upvote.model.User;
import br.com.aol.posts.upvote.payload.ChoiceResponse;
import br.com.aol.posts.upvote.payload.PostResponse;

public class ModelMapper {

	public static PostResponse mapPostToPostResponse(final Post post, final Map<Long, Long> choiceVotesMap, final User creator, final Long userVote) {

		final PostResponse postResponse = new PostResponse(post, choiceVotesMap, creator);

		if (userVote != null) {
			postResponse.setSelectedChoice(userVote);
		}

		postResponse.setTotalVotes(postResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum());

		return postResponse;
	}

}
