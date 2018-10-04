package br.com.aol.posts.upvote.payload;
import javax.validation.constraints.NotNull;

public class VoteRequest {
    @NotNull
    private Long choiceId;

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(final Long choiceId) {
        this.choiceId = choiceId;
    }
}

