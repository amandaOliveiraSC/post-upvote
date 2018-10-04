package br.com.aol.posts.upvote.payload;

import br.com.aol.posts.upvote.model.User;

public class UserSummary {

	private Long id;
	private String username;
	private String name;

	public UserSummary(final Long id, final String username, final String name) {

		this.id = id;
		this.username = username;
		this.name = name;
	}

	public UserSummary(final User user) {

		this.id = user.getId();
		this.username = user.getUsername();
		this.name = user.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
