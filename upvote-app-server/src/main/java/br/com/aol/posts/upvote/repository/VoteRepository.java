package br.com.aol.posts.upvote.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.aol.posts.upvote.model.ChoiceVoteCount;
import br.com.aol.posts.upvote.model.Vote;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
	
    @Query("SELECT NEW br.com.aol.posts.upvote.model.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.post.id in :postIds GROUP BY v.choice.id")
    List<ChoiceVoteCount> countByPostIdInGroupByChoiceId(@Param("postIds") final List<Long> postIds);

    @Query("SELECT NEW br.com.aol.posts.upvote.model.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.post.id = :postId GROUP BY v.choice.id")
    List<ChoiceVoteCount> countByPostIdGroupByChoiceId(@Param("postId") final Long postId);

    @Query("SELECT v FROM Vote v where v.user.id = :userId and v.post.id in :postIds")
    List<Vote> findByUserIdAndPostIdIn(@Param("userId") final Long userId, @Param("postIds") final List<Long> postIds);

    @Query("SELECT v FROM Vote v where v.user.id = :userId and v.post.id = :postId")
    Vote findByUserIdAndPostId(@Param("userId") final Long userId, @Param("postId") final Long postId);

    @Query("SELECT COUNT(v.id) from Vote v where v.user.id = :userId")
    long countByUserId(@Param("userId") final Long userId);

    @Query("SELECT v.post.id FROM Vote v WHERE v.user.id = :userId")
    Page<Long> findVotedPostIdsByUserId(@Param("userId") final Long userId, final Pageable pageable);
}

