package com.snb.ms.posts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Posts", description = "Operations for external posts resource consumption")
@Slf4j
public class PostsController {

    private final PostsService postsService;

    @GetMapping
    @Operation(summary = "Fetch all posts from JSONPlaceholder")
    @ApiResponse(responseCode = "200", description = "Posts fetched successfully")
    public ResponseEntity<List<PostDto>> findAll() {
        log.debug("Received request to fetch all posts");
        List<PostDto> posts = postsService.findAll();
        log.info("Fetched {} posts", posts.size());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch a post by id from JSONPlaceholder")
    @ApiResponse(responseCode = "200", description = "Post fetched successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
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
