package br.com.aol.posts.upvote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aol.posts.upvote.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(final String email);

    Optional<User> findByUsernameOrEmail(final String username, final String email);

    List<User> findByIdIn(final List<Long> userIds);

    Optional<User> findByUsername(final String username);

    Boolean existsByUsername(final String username);

    Boolean existsByEmail(final String email);
}
