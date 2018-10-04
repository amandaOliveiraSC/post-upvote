package br.com.aol.posts.upvote.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class PostRequest {
	
    @NotBlank
    @Size(max = 140)
    private String question;

    @NotNull
    @Size(min = 2, max = 6)
    @Valid
    private List<ChoiceRequest> choices;

    @NotNull
    @Valid
    private PostLength postLength;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public List<ChoiceRequest> getChoices() {
        return choices;
    }

    public void setChoices(final List<ChoiceRequest> choices) {
        this.choices = choices;
    }

    public PostLength getPostLength() {
        return postLength;
    }

    public void setPostLength(final PostLength postLength) {
        this.postLength = postLength;
    }
}
