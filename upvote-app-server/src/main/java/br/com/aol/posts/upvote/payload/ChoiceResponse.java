package br.com.aol.posts.upvote.payload;

import br.com.aol.posts.upvote.model.Choice;

public class ChoiceResponse {

	public ChoiceResponse() {

	}

	public ChoiceResponse(final Choice choice) {
		this.id = choice.getId();
		this.text = choice.getText();
	}

	private long id;
	private String text;
	private long voteCount;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(final long voteCount) {
		this.voteCount = voteCount;
	}

}
