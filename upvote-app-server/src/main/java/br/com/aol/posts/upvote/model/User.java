package br.com.aol.posts.upvote.model;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NaturalId;

import br.com.aol.posts.upvote.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }), @UniqueConstraint(columnNames = { "email" }) })
public class User extends DateAudit {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 40)
	private String name;

	@NotBlank
	@Size(max = 15)
	private String username;

	@NaturalId
	@NotBlank
	@Size(max = 40)
	@Email
	private String email;

	@NotBlank
	@Size(max = 100)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {

	}

	public User(final String name, final String username, final String email, final String password) {
		super();

		this.setCreatedAt(Instant.now());
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public User(final String firstName, final String lastName, final String username, final String email, final String password) {
		super();

		this.name = firstName + lastName;

		if (StringUtils.isBlank(username)) {

			this.username = firstName + "." + lastName;

			if (this.username.length() > 15) {
				this.username = StringUtils.abbreviate(firstName, 7) + "." + StringUtils.abbreviate(lastName, 7);
			}

		} else {
			this.username = username;
		}

		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	
	@Override
	public String toString() {
		return "id=[" + this.id + "] name=[" + this.name + "] username=[" + this.username + "] password=[" + this.password + "] createAt=[" + this.getCreatedAt() + "].";
	}
	
	
	
}