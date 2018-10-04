package br.com.aol.posts.upvote.payload;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.aol.posts.upvote.model.Post;
import br.com.aol.posts.upvote.model.User;

public class PostResponse {
	
	public PostResponse() {
	}
	
	public PostResponse(final Post post) {
		this.id = post.getId();
		this.question = post.getQuestion();
		this.creationDateTime = post.getCreatedAt();
		this.expirationDateTime = post.getExpirationDateTime();
		this.isExpired = post.getExpirationDateTime().isBefore(Instant.now());
	}

	
	public PostResponse(final Post post, final Map<Long, Long> choiceVotesMap, final User creator) {
		this.id = post.getId();
		this.question = post.getQuestion();
		this.creationDateTime = post.getCreatedAt();
		this.expirationDateTime = post.getExpirationDateTime();
		this.isExpired = post.getExpirationDateTime().isBefore(Instant.now());
		this.createdBy = new UserSummary(creator);
		
		this.choices = post.getChoices().stream().map(choice -> {
			ChoiceResponse choiceResponse = new ChoiceResponse(choice);

			if (choiceVotesMap.containsKey(choice.getId())) {
				choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
			} else {
				choiceResponse.setVoteCount(0);
			}
			
			return choiceResponse;
		}).collect(Collectors.toList());
		
			
	}
	
    private Long id;
    private String question;
    private List<ChoiceResponse> choices;
    private UserSummary createdBy;
    private Instant creationDateTime;
    private Instant expirationDateTime;
    private Boolean isExpired;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long selectedChoice;
    private Long totalVotes;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public List<ChoiceResponse> getChoices() {
        return choices;
    }

    public void setChoices(final List<ChoiceResponse> choices) {
        this.choices = choices;
    }

    public UserSummary getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final UserSummary createdBy) {
        this.createdBy = createdBy;
    }


    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(final Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Instant getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(final Instant expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(final Boolean expired) {
        isExpired = expired;
    }

    public Long getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(final Long selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(final Long totalVotes) {
        this.totalVotes = totalVotes;
    }
}
