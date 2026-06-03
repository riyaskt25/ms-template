package com.snb.ms.shared;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "[999] - favicon-controller", description = "Favicon endpoint")
public class FaviconController {

  @GetMapping("/favicon.ico")
  @Operation(
      summary = "Favicon endpoint",
      description = "No-content favicon endpoint used by browsers.")
  public ResponseEntity<Void> favicon() {
    return ResponseEntity.noContent().build();
  }
}
