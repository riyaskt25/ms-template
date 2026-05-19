package com.snb.ms.posts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "External post response payload")
public class PostDto {
    @Schema(description = "External author identifier", example = "1")
    private Long userId;

    @Schema(description = "External post identifier", example = "1")
    private Long id;

    @Schema(description = "Post title", example = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
    private String title;

    @Schema(description = "Post body content", example = "quia et suscipit suscipit recusandae consequuntur expedita et cum")
    private String body;
}
