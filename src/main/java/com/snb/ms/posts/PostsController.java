package com.snb.ms.posts;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PostsController implements PostsApi {

    private final PostsService postsService;

    @Override
    @GetMapping
    public ResponseEntity<List<PostDto>> findAll() {
        log.debug("Received request to fetch all posts");
        List<PostDto> posts = postsService.findAll();
        log.info("Fetched {} posts", posts.size());
        return ResponseEntity.ok(posts);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> findById(@PathVariable @Positive(message = "id must be positive") Long id) {
        log.debug("Received request to fetch post by id={}", id);
        Optional<PostDto> post = postsService.findById(id);
        if (post.isPresent()) {
            log.info("Post found for id={}", id);
            return ResponseEntity.ok(post.get());
        }
        log.info("Post not found for id={}", id);
        return ResponseEntity.notFound().build();
    }
}
