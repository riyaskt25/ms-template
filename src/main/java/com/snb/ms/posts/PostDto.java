package com.snb.ms.posts;

import com.snb.ms.shared.BaseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostDto extends BaseResponseDTO {
    private Long userId;
    private Long id;
    private String title;
    private String body;
}
