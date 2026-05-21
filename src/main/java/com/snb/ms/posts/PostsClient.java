package com.snb.ms.posts;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostsClient {

    private final RestClient postsRestClient;

    public List<PostExternalResponse> fetchAllPosts() {
        log.debug("Fetching all external posts");
        try {
            List<PostExternalResponse> posts = postsRestClient.get()
                    .uri("/posts")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return posts == null ? Collections.emptyList() : posts;
        } catch (RestClientException ex) {
            log.error("Failed to fetch posts from external API", ex);
            throw new IllegalStateException("Unable to fetch posts from external API", ex);
        }
    }

    public Optional<PostExternalResponse> fetchPostById(Long id) {
        log.debug("Fetching external post by id={}", id);
        try {
            PostExternalResponse post = postsRestClient.get()
                    .uri("/posts/{id}", id)
                    .retrieve()
                    .body(PostExternalResponse.class);
            return Optional.ofNullable(post);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.info("External post not found id={}", id);
                return Optional.empty();
            }
            log.error("Failed to fetch post id={} from external API", id, ex);
            throw new IllegalStateException("Unable to fetch post from external API", ex);
        } catch (RestClientException ex) {
            log.error("Failed to fetch post id={} from external API", id, ex);
            throw new IllegalStateException("Unable to fetch post from external API", ex);
        }
    }
}
