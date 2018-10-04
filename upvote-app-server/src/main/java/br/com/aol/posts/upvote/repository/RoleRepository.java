package br.com.aol.posts.upvote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aol.posts.upvote.model.Role;
import br.com.aol.posts.upvote.model.RoleName;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(final RoleName roleName);
}
