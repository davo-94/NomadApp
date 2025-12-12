package cl.vasquez.nomadapp.repository;

import cl.vasquez.nomadapp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
    List<Post> findByPublishedTrueOrderByCreatedAtDesc();
    
    @Query("SELECT p FROM Post p WHERE p.published = true ORDER BY p.createdAt DESC")
    List<Post> findAllPublishedPosts();
}
