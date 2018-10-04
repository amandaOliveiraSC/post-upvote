package br.com.aol.posts.upvote.payload;

public class UserIdentityAvailability {
	
    private Boolean available;

    public UserIdentityAvailability(final Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(final Boolean available) {
        this.available = available;
    }
}
