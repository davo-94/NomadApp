package cl.vasquez.nomadapp.service;

import cl.vasquez.nomadapp.dto.PostRequest;
import cl.vasquez.nomadapp.entity.Post;
import cl.vasquez.nomadapp.entity.User;
import cl.vasquez.nomadapp.repository.PostRepository;
import cl.vasquez.nomadapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Post createPost(PostRequest request, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .author(userOpt.get())
                .published(request.getPublished() != null ? request.getPublished() : true)
                .likes(0)
                .build();

        return postRepository.save(post);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getPublicPosts() {
        return postRepository.findAllPublishedPosts();
    }

    public List<Post> getUserPosts(Long userId) {
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(userId);
    }

    public Post updatePost(Long postId, PostRequest request, Long userId) {
        Optional<Post> postOpt = postRepository.findById(postId);

        if (postOpt.isEmpty()) {
            throw new RuntimeException("Post no encontrado");
        }

        Post post = postOpt.get();

        // Verificar que el usuario sea el autor o admin
        if (!post.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para editar este post");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        post.setPublished(request.getPublished());
        post.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    public void deletePost(Long postId, Long userId) {
        Optional<Post> postOpt = postRepository.findById(postId);

        if (postOpt.isEmpty()) {
            throw new RuntimeException("Post no encontrado");
        }

        Post post = postOpt.get();

        // Verificar que el usuario sea el autor o admin
        if (!post.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para eliminar este post");
        }

        postRepository.delete(post);
    }

    public Post likePost(Long postId) {
        Optional<Post> postOpt = postRepository.findById(postId);

        if (postOpt.isEmpty()) {
            throw new RuntimeException("Post no encontrado");
        }

        Post post = postOpt.get();
        post.setLikes(post.getLikes() + 1);
        post.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }
}
