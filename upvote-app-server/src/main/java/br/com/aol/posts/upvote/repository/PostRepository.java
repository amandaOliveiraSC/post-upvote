package br.com.aol.posts.upvote.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.aol.posts.upvote.model.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(final Long postId);

    Page<Post> findByCreatedBy(final Long userId, final Pageable pageable);

    long countByCreatedBy(final Long userId);

    List<Post> findByIdIn(final List<Long> postIds);

    List<Post> findByIdIn(final List<Long> postIds, final Sort sort);
}
