package com.snb.ms.posts;

import lombok.Data;

@Data
public class PostExternalResponse {
  private Long userId;
  private Long id;
  private String title;
  private String body;
}
