package com.snb.ms.posts;

import com.snb.ms.shared.BaseResponseDTO;
import com.snb.ms.shared.api.CommonApiParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Tag(name = "[999] - Posts", description = "Operations for external posts resource consumption")
public interface PostsApi {

  @Operation(
      operationId = "getAllExternalPosts",
      summary = "List external posts",
      description =
          "Fetches posts from the configured external provider (default: JSONPlaceholder).")
  @CommonApiParameters
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Posts fetched successfully",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostDto.class)))),
    @ApiResponse(
        responseCode = "502",
        description = "Upstream provider call failed",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "GetAllExternalPostsUpstreamError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"UPSTREAM_ERROR\",\n      \"code\": \"UPSTREAM_UNAVAILABLE\",\n      \"message\": \"External posts provider is unavailable\",\n      \"description\": \"Failed to fetch posts from upstream provider (connection timeout)\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "ListPostsInternalError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch external posts\",\n      \"description\": \"Unexpected error while transforming upstream posts response\"\n    }\n  ]\n}")))
  })
  List<PostDto> findAll();

  @Operation(
      operationId = "getExternalPostById",
      summary = "Get external post by id",
      description = "Fetches one external post by identifier from the configured provider.")
  @CommonApiParameters
  @Parameters({
    @Parameter(
        name = "id",
        description = "External post identifier",
        required = true,
        example = "1")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Post fetched successfully",
        content = @Content(schema = @Schema(implementation = PostDto.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Post not found",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "GetExternalPostByIdNotFoundError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"NOT_FOUND\",\n      \"code\": \"RESOURCE_NOT_FOUND\",\n      \"message\": \"Resource not found\",\n      \"description\": \"Post not found for id=999\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "502",
        description = "Upstream provider call failed",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "GetExternalPostByIdUpstreamError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"UPSTREAM_ERROR\",\n      \"code\": \"UPSTREAM_UNAVAILABLE\",\n      \"message\": \"External posts provider is unavailable\",\n      \"description\": \"Failed to fetch post id=1 from upstream provider (connection timeout)\"\n    }\n  ]\n}"))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content =
            @Content(
                schema = @Schema(implementation = BaseResponseDTO.class),
                examples =
                    @ExampleObject(
                        name = "GetPostInternalError",
                        value =
                            "{\n  \"errors\": [\n    {\n      \"type\": \"SERVER_ERROR\",\n      \"code\": \"INTERNAL_ERROR\",\n      \"message\": \"Failed to fetch external post\",\n      \"description\": \"Unexpected error while processing upstream response for id=1\"\n    }\n  ]\n}")))
  })
  PostDto findById(@Positive(message = "{validation.common.id.positive}") Long id);
}
