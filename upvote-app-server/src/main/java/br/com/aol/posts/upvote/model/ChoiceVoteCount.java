package br.com.aol.posts.upvote.model;

public class ChoiceVoteCount {
    private Long choiceId;
    private Long voteCount;
    
    public ChoiceVoteCount() {
    	
    }

    public ChoiceVoteCount(Long choiceId, Long voteCount) {
    	super();
    	
        this.choiceId = choiceId;
        this.voteCount = voteCount;
    }

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }
}

