package com.snb.ms.posts;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {

  private final PostsClient postsClient;

  public List<PostDto> findAll() {
    log.debug("Fetching all posts from external provider");
    List<PostDto> posts = postsClient.fetchAllPosts().stream().map(this::toDto).toList();
    log.info("Fetched {} external posts", posts.size());
    return posts;
  }

  public Optional<PostDto> findById(Long id) {
    log.debug("Fetching post by id={} from external provider", id);
    Optional<PostDto> post = postsClient.fetchPostById(id).map(this::toDto);
    log.info("External post lookup id={} found={}", id, post.isPresent());
    return post;
  }

  private PostDto toDto(PostExternalResponse source) {
    PostDto dto = new PostDto();
    dto.setId(source.getId());
    dto.setUserId(source.getUserId());
    dto.setTitle(source.getTitle());
    dto.setBody(source.getBody());
    return dto;
  }
}
