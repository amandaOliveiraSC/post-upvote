package br.com.aol.posts.upvote.runner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import br.com.aol.posts.upvote.model.Choice;
import br.com.aol.posts.upvote.model.Post;
import br.com.aol.posts.upvote.model.Role;
import br.com.aol.posts.upvote.model.RoleName;
import br.com.aol.posts.upvote.model.User;
import br.com.aol.posts.upvote.model.Vote;
import br.com.aol.posts.upvote.repository.PostRepository;
import br.com.aol.posts.upvote.repository.RoleRepository;
import br.com.aol.posts.upvote.repository.UserRepository;
import br.com.aol.posts.upvote.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataBaseRunner implements ApplicationRunner {

	private RoleRepository roleRepository;

	private UserRepository userRepository;

	private PostRepository postRepository;

	private VoteRepository voteRepository;

	private Random random = new Random();

	private Lorem lorem = LoremIpsum.getInstance();

	@Autowired
	public DataBaseRunner(final UserRepository userRepository, final PostRepository postRepository, final VoteRepository voteRepository, final RoleRepository roleRepository) {

		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.voteRepository = voteRepository;
		this.roleRepository = roleRepository;
	}

	public void run(final ApplicationArguments applicationArguments) {
		try {
			createRoles();
			List<User> userList = createUsers();
			List<Post> postList = posting(userList);
			voting(userList, postList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void createRoles() {
		roleRepository.save(new Role(RoleName.ROLE_ADMIN));
		roleRepository.save(new Role(RoleName.ROLE_USER));
	}

	private List<User> createUsers() {

		log.info("\n\n\n####################################### INSERTING USERS [BEGIN] ###########################################");
		List<User> userList = new ArrayList<User>();

		for (User user : getUsers()) {
			user = userRepository.save(user);
			userList.add(user);
			log.error("User saved: {}.", userList.get(userList.size() - 1));
		}

		log.info("\n\n\n####################################### INSERTING USERS [END] ############################################");

		return userList;
	}

	private List<Post> posting(List<User> userList) {

		List<Post> postList = new ArrayList<Post>();

		for (Post post : getPosts(userList)) {
			postList.add(postRepository.save(post));
			log.debug("Post saved: {}.", postList.get(postList.size() - 1));
		}

		return postList;
	}

	private void voting(List<User> userList, List<Post> postList) {
		voteRepository.saveAll(getVotes(userList, postList));
	}

	private List<Vote> getVotes(List<User> userList, List<Post> postList) {
		List<Vote> voteList = new ArrayList<Vote>();
		int min = 0;
		int max;

		for (User user : userList) {
			for (Post post : postList) {
				max = post.getChoices().size() - 1;
				voteList.add(new Vote(user, post, post.getChoices().get(random.nextInt(max - min + 1) + min)));
			}
		}

		return voteList;
	}

	private List<User> getUsers() {

		List<User> userList = new ArrayList<User>();

		for (int i = 0; i < 10; i++) {
			userList.add(new User(lorem.getFirstNameFemale(), lorem.getLastName(), null, lorem.getEmail(), "p4ss!"));
		}

		return userList;
	}

	private List<Post> getPosts(List<User> userList) {

		List<Post> postList = new ArrayList<Post>();
		Post post;

		int min = 2;
		int max = 6;
		int number;

		for (User user : userList) {
			for (int i = 0; i < 10; i++) {

				post = new Post(user, lorem.getWords(3, 7), getRandomInstant());

				postList.add(post);

				number = random.nextInt(max - min + 1) + min;

				for (int j = 0; j < number; j++) {
					post.addChoice(new Choice(lorem.getWords(1, 3), post));
				}
			}
		}

		return postList;
	}

	private Instant getRandomInstant() {

		int min = 1;
		int max = 30;

		int number = random.nextInt(max - min + 1) + min;

		if (number > 12) {
			return Instant.now().plus(number, ChronoUnit.HOURS);
		}

		return Instant.now().plus(number, ChronoUnit.DAYS);

	}
}
