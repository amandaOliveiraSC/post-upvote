package br.com.aol.posts.upvote.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.aol.posts.upvote.model.audit.UserDateAudit;

@Entity
@Table(name = "Posts")
public class Post extends UserDateAudit {

	private static final long serialVersionUID = 1L;

	public Post() {
		super();

	}

	public Post(final User user, final String question, final Instant expirationDateTime, final Choice... choice) {
		super();

		this.setCreatedBy(user.getId());
		this.setCreatedAt(Instant.now());
//		this.user = user;
		this.question = question;
		this.expirationDateTime = expirationDateTime;
		this.choices = new ArrayList<Choice>(Arrays.asList(choice));
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 140)
	private String question;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@Size(min = 2, max = 6)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 30)
	private List<Choice> choices = new ArrayList<Choice>();

	@NotNull
	private Instant expirationDateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}

	public Instant getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(Instant expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public void addChoice(Choice choice) {
		choices.add(choice);
		choice.setPost(this);
	}

	public void removeChoice(Choice choice) {
		choices.remove(choice);
		choice.setPost(null);
	}

	private String getChoicesToString() {

		StringBuilder choicesAsString = new StringBuilder();

		for (Choice choice : choices) {
			choicesAsString.append(choice.toString());
		}

		return choicesAsString.toString();
	}

	@Override
	public String toString() {
		return "id=[" + this.id + "] question=[" + this.question + "] createBy=[" + this.getCreatedBy() + "] choices=[" + this.getChoicesToString() + "] createAt=[" + this.getCreatedAt() + "].";
	}
}
