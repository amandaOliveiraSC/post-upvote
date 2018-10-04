package br.com.aol.posts.upvote.payload;

import java.time.Instant;

import br.com.aol.posts.upvote.model.User;

public class UserProfile {
	
    private Long id;
    private String username;
    private String name;
    private Instant joinedAt;
    private Long postCount;
    private Long voteCount;

    public UserProfile(final Long id, final String username, final String name, final Instant joinedAt, final Long postCount, final Long voteCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.postCount = postCount;
        this.voteCount = voteCount;
    }

    public UserProfile(final User user, final Long postCount, final Long voteCount) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.joinedAt = user.getCreatedAt();
        this.postCount = postCount;
        this.voteCount = voteCount;
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

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(final Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Long getPostCount() {
        return postCount;
    }

    public void setPostCount(final Long postCount) {
        this.postCount = postCount;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(final Long voteCount) {
        this.voteCount = voteCount;
    }
}
