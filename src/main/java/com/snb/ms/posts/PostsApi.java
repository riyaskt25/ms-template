package com.snb.ms.posts;

import com.snb.ms.shared.BaseResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Tag(name = "Posts", description = "Operations for external posts resource consumption")
public interface PostsApi {

    @Operation(
        operationId = "getAllExternalPosts",
        summary = "List external posts",
        description = "Fetches posts from the configured external provider (default: JSONPlaceholder)."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Posts fetched successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostDto.class)))
        ),
        @ApiResponse(
            responseCode = "502",
            description = "Upstream provider call failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    List<PostDto> findAll();

    @Operation(
        operationId = "getExternalPostById",
        summary = "Get external post by id",
        description = "Fetches one external post by identifier from the configured provider."
    )
    @Parameters({
        @Parameter(ref = "#/components/parameters/XRequestIdHeader"),
        @Parameter(ref = "#/components/parameters/XTenantIdHeader"),
        @Parameter(ref = "#/components/parameters/AcceptLanguageHeader"),
        @Parameter(name = "id", description = "External post identifier", required = true, example = "1")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Post fetched successfully",
            content = @Content(schema = @Schema(implementation = PostDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "Post not found"),
        @ApiResponse(
            responseCode = "502",
            description = "Upstream provider call failed",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = BaseResponseDTO.class))
        )
    })
    PostDto findById(@Positive(message = "{validation.common.id.positive}") Long id);
}