package com.snb.ms.posts;

import com.snb.ms.exception.ResourceNotFoundException;
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
    public ResponseEntity<PostDto> findById(@PathVariable Long id) {
        log.debug("Received request to fetch post by id={}", id);
        PostDto post = postsService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("postId=" + id));
        log.info("Post found for id={}", id);
        return ResponseEntity.ok(post);
    }
}
